# blocklog

A Friday afternoon experiment to create an append-only tamper proof log.

It's a mashup between two projects I found:

https://github.com/mrwilson/java-dirty
and
https://github.com/eugenp/tutorials/tree/master/java-blockchain

The block store needed support for byte[] storage as it was originally written for primitive data types only.

I also added auto compression for storing large string data: Byte512


It's a solution looking for the right problem... 