/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.helper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A Helper is a function that is related to a Java class. It can do things for this
 * Java type. The repository is a data structure to find the most specific helper for 
 * a class.
 * 
 * @param <T> A Helper implementation.
 */
public class ClassHelperRepository<T extends ClassHelper>
{
    private HelperTreeNode<T> root;

    private static class HelperTreeNode<T extends ClassHelper>
    {
        private T helper;
        private List<HelperTreeNode<T>> children;

        public HelperTreeNode(T aClass)
        {
            helper = aClass;
            children = new LinkedList<HelperTreeNode<T>>();
        }

        public T getHelper()
        {
            return helper;
        }

        public boolean insertNode(HelperTreeNode<T> aNode)
        {
            if(aNode.getHelper().getHelpedClass() == helper.getHelpedClass())
            {
                // The current tree node already contains a helper for that class,
                // in this case we replace the helper!
                helper = aNode.getHelper();
                return true;
            }
            else if(helper.getHelpedClass().isAssignableFrom(aNode.getHelper().getHelpedClass()))
            {
                // The new node is a helper for a more specific class then the
                // current helper.

                // First we are going to test recursively if the new node
                // is a more specific helper then one of our children.

                boolean insertedToSomeChild = false;
                for (HelperTreeNode<T> child : children)
                {
                    boolean lSuccess = child.insertNode(aNode);
                    if (lSuccess)
                    {
                        insertedToSomeChild = true;
                        break;
                    }
                }

                // If we get here, the new node contains a helper that does not
                // belong to one of our children, so we can add it as a new child.
                if(!insertedToSomeChild)
                {
                    // Rebalance tree. We have to rebuild the tree because the children might actually
                    // belong to the new node. This happens when the new node is more specific then the
                    // current node but less specific then one of the children.
                    //
                    final Iterator<HelperTreeNode<T>> lIter2 = children.iterator();
                    while(lIter2.hasNext())
                    {
                        final HelperTreeNode<T> lChild = lIter2.next();
                        if(aNode.getHelper().getHelpedClass().isAssignableFrom(lChild.getHelper().getHelpedClass()))
                        {
                            lIter2.remove();
                            aNode.insertNode(lChild);
                        }
                    }

                    // Add the new balanced tree.
                    children.add(aNode);
                }
                return true;
            }
            else
                return false;
        }

        /** Core finder algorithm
         *
         * @param aClass The class for which we want to find a helper.
         * @return A Helper or null if no applicable helper could be found. We first try to
         * find an exact match, and if it cannot be done, we try to find a mapper for the closest parent class.
         */
        T findHelper(Class<?> aClass)
        {
            // If we have an exact match, we return the helper.
            // This is the perfect case.
            if(helper.getHelpedClass() == aClass) return helper;
            else
            {
                // If we do not have an exact match, we go for the
                // more specific match.
                for (HelperTreeNode<T> lChildNode : children)
                {
                    final T lHelper = lChildNode.findHelper(aClass);
                    if (lHelper != null) return lHelper;
                }
            }

            // If the current helper is not an exact match, and none of the
            // subclasses (finer grained) provide a match, we test if the
            // current helper might be applicable to the more specific class.
            // In this case, we might loose information, but it is better than
            // doing nothing. This case also lets us implement general mappers.
            if(helper.getHelpedClass().isAssignableFrom(aClass)) return helper;
            else return null;
        }

        public String prettyPrint(String aIndent)
        {
            StringBuilder lBld = new StringBuilder(aIndent);
            lBld.append(helper.getHelpedClass().getName());
            for(HelperTreeNode<T> lChild : children)
            {
                lBld.append("\n");
                lBld.append(lChild.prettyPrint(aIndent + "   "));
            }
            return lBld.toString();
        }
    }

    private static class RootHelper
    implements ClassHelper
    {
        public Class<?> getHelpedClass()
        {
            return Object.class;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ClassHelperRepository()
    {
        root = new HelperTreeNode(new RootHelper());
    }

    /**
     * Add a helper to the repository.
     * @param aHelper   The helper to add.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addHelper(T aHelper)
    {
        root.insertNode(new HelperTreeNode(aHelper));
    }

    /**
     * Lookup a helper in the repository.
     * @param aClass The class for which a helper is wanted.
     * @return The corresponding helper. There is always a general fallback helper which uses introspection to
     *         serialize the properties of a JavaBean. This property helper is always returned as a last possibility.
     *         So this method always returns a helper.
     */
    public T findHelper(Class<?> aClass)
    {
        T helper = root.findHelper(aClass);
        if(helper instanceof RootHelper) return null;
        else return helper;
    }

    public String prettyPrint()
    {
        return root.prettyPrint("");
    }
}
