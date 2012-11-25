/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.helper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HelperRepository<T extends Helper>
{
    private HelperTreeNode<T> root;

    private static class HelperTreeNode<T extends Helper>
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
                helper = aNode.getHelper();
                return true;
            }
            else if(helper.getHelpedClass().isAssignableFrom(aNode.getHelper().getHelpedClass()))
            {
                boolean insertedToSomeChild = false;
                for (HelperTreeNode<T> lChildren : children)
                {
                    boolean lSuccess = lChildren.insertNode(aNode);
                    if (lSuccess)
                    {
                        insertedToSomeChild = true;
                        break;
                    }
                }

                // Add node
                if(!insertedToSomeChild)
                {
                    // Rebalance tree.
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
    implements Helper
    {
        public Class<?> getHelpedClass()
        {
            return Object.class;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public HelperRepository()
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
        return root.findHelper(aClass);
    }

    public String prettyPrint()
    {
        return root.prettyPrint("");
    }
}
