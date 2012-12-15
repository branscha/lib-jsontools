# Project Tasks
## To do

* -

## Done

* 2012-11-15: A ValidatorBuilder to create a validator in code; just like the JPA QueryBuilder but not so complicated.
* 2012-11-28: I finally found out how you can manipulate primitive arrays using the Array class. I already reviewed the ArrayMapper but the ArrayHelper in the serializer should be rewritten as well.
* 2012-11-27: Adding a helper for a class that is already in the repository will replace the existing helper. Make note of this in the docs.
* 2012-11-27: Add separate tests for the helper repository using mockito mocks for the helpers. Try out all sorts of inheritance situations.
* 2012-11-26: Add the direction JSON->Java or Java->JSON to each mapper message.
* 2012-11-26: Review date mapper, simplify to Java essentials or add configuration options that are picked up. I don't understand how the current mapper got the functionality it has right now ...
* 2012-11-25: Review error messages JSONMapper + helpers.
* 2012-11-25: Review error messages JSONParser, JSONSerializer + helpers.
* 2012-11-25: Review JSONMarshal (serializer) for context params, just like the mapper.
* 2012-11-23: The JSONMapper is static class, static functions. The repository is global. The option to map beans using getters/setters or fields is implemented by modifying the repository. This has impact on all following mappings! Better would be to pass a map of options Map<String, Object> that is passed along to each mapper for each mapping. The user can pass a number of flags to influence the mappings.
* 2012-11-24: Review JUnit tests. Remove println's because these don't actually do tests. Rewrite using more thorough tests with Hamcrest.
* 2012-11-22: Removed ANTLR dependency and added a simple parser (from the jsonutil project).
* 2012-11-22: Changed to MIT license.
