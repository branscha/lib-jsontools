# Project Tasks

* 2012-11-22: In de JSONParser, parseJsonObject(), the error handling when the key is not a string should be improved.
* 2012-11-22: Copy line numbers from the tokenizer to the JSONValues.
* 2012-11-22: Remove pos within the line, the tokenizer does not keep track, and we can't know how much whitespace is lost. 