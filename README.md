PBEKeyTester
============

Purpose:
This application allows users on any Android device to determine the optimal trade-off between security and convenience. The more iterations that occur, the more difficult the key will be to brute-force but the longer the process takes the longer the user has to wait that provides a less convenient user exprience.


Benchmark format: Iteration - Time


Benchmarks from Nexus 4:

Algorithm (SecretKeyFactory instance): PBEWITHSHA1AND128BITAES-CBC-BC

Password (10 characters): thisisatest

Round 1

15900 - 388ms

16000 - 581ms

Round 2

19700 - 482ms

19800 - 505ms

Round 3

17900 - 481ms

18000 - 531ms

Round 4

16800 - 551ms

16700 - 451ms


Great examples of proper implementations:
https://github.com/WhisperSystems/TextSecure/blob/master/src/org/thoughtcrime/securesms/crypto/MasterSecretUtil.java

https://github.com/nelenkov/android-pbe/blob/master/src/org/nick/androidpbe/MainActivity.java
http://nelenkov.blogspot.com/2012/04/using-password-based-encryption-on.html
