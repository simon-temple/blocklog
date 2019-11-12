# blocklog

A Friday afternoon experiment to create an append-only tamper proof log.

It's a mashup between two projects I found:

https://github.com/mrwilson/java-dirty
and
https://github.com/eugenp/tutorials/tree/master/java-blockchain

The block store needed support for byte[] storage as it was originally written for primitive data types only.

I also added auto compression for storing large string data: Byte512


It's a solution looking for the right problem... 

```java
// Open/create the log in the filesystem 
SecureLog log = new SecureLog("/opt/test.log");
 
// Add your String data to the log
log.add("1987234912673509193579159719375109345797073547234875");
 
// Dump the log blocks to stdout
log.dump();

// Test the log has not been messed with
Assert.assertTrue(log.isValid());

// Close it
log.close();
```