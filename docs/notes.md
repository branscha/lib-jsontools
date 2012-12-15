# Project Notes
## Design Decisions

## Possible Enhancements

* Add paths to navigate JSON data structures just like in jsonutil.
* Add JAX-RS MessageBodyReader/Writer so that the tools can be used in JAX-RS.
* Add a MapperFacade that contains: parser, mapper and validator. So the user can use a single class to accomplish mappings.
* Add a SerializerFacade that contains: parser, serializer and validator so that the user can use a single class to accomplish serialization.
