#Introduction
#About JSON 
JSON (JavaScript Object Notation) is a file format to represent data. It is similar to XML but has different characteristics. It is suited to represent configuration information, implement communication protocols and so on. XML is more suited to represent annotated documents. JSON parsing is very fast, the parser can be kept lean and mean. It is easy for humans to read and write. It is based on a subset of the JavaScript Programming Language, Standard ECMA-262 3rd Edition - December 1999. JSON is a text format that is completely language independent but uses conventions that are familiar to programmers of the C-family of languages, including C, C++, C#, Java, JavaScript, Perl, Python, and many others. These properties make JSON an ideal data-interchange language. The format is specified on [json.org](http://www.json.org/), for the details please visit this site.

JSON is a very simple format. As a result, the parsing and rendering is fast and easy, you can concentrate on the content of the file in stead of the format. In XML it is often difficult to fully understand all features (e.g. name spaces, validation, ...). As a result, XML tends to become part of the problem i.s.o. the solution. In JSON everything is well defined, all aspects of the representation are clear, you can concentrate on how you are going to represent your application concepts.

Following tools are available:

* **Parser**: Parse JSON text files and convert these to a Java model.
* **Renderer**: Render a Java representation into text.
* **Serializer**: Serialize plain POJO clusters to a JSON representation. The goal is to provide a serializing mechanism which can cope with all kinds of Java datastructures (recursion, references, primitive types, ...) .
* **Mapper**: Map POJO to JSON, this time the JSON text should be as clean as possible. This tool is the best choice when data has to be communicated between Java and other programming languages who can parse JSON.
* **Validator**: Validate the contents of a JSON file using a JSON schema.

## Project

The project files are located on [Github/lib-jsontools](https://github.com/branscha/lib-jsontools)
## License

The library is released under the MIT License. Previous versions < 2.0 were released with LGPL but I like the MIT license more because of its freedom.

## Latest News

* **2012-12**. Preparing release 2.0. The biggest change is the replacement of the ANTLR parser with a handcrafted parser which is much faster.
* **2012-11**. Transferring the JSONTools project from Berlios to GitHub. I like GIT more than SVN and since Berlios was shutting down last year I have more trust in GitHub.