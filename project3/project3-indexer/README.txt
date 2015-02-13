Team: RKC
-----------------------------------
We created 3 indexes on the following:
	1) iid, StringField
	2) name, StringField
	3) content, TextField

The indexes on iid and name are both stored because we want to retrieve
the itemId and name and return it to the user. They are both StringFields 
because they should not be tokenized and should be treated as a single
value. The index on content is the equivalent of the union of name,
description, and category attributes and we do not store it because
we simply need to search on it. It is a TextField because we want to
be able to match on any keyword contained.