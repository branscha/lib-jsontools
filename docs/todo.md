# Project Tasks
## Todo

* 2012-11-25: Review JSONMarshal (serializer) for context params, just like the mapper.
* 2012-11-25: Review JSONParser for context params, just like the mapper.

## Done

* 2012-11-23: The JSONMapper is static class, static functions. The repository is global. The option to map beans using getters/setters or fields is implemented by modifying the repository. This has impact on all following mappings! Better would be to pass a map of options Map<String, Object> that is passed along to each mapper for each mapping. The user can pass a number of flags to influence the mappings.
* 2012-11-24: Review JUnit tests. Remove println's because these don't actually do tests. Rewrite using more thorough tests with Hamcrest.
