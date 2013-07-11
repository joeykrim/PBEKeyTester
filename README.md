PBEKeyTester
============

Purpose:
This application allows users on any Android device to determine the optimal trade-off between security and convenience. The more iterations that occur, the more difficult the key will be to brute-force but the longer the process takes the longer the user has to wait that provides a less convenient user exprience.


Benchmark format: Iteration Count - Time


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



Benchmarks from Nexus 10:

Algorithm (SecretKeyFactory instance): PBEWITHSHA1AND128BITAES-CBC-BC

Password (10 characters): thisisatest

Round 1

43100 - 508ms

43000 - 442ms

Round 2

41000 - 512ms

40900 - 458ms

Round 3

41800 - 506ms

41700 - 437ms



Benchmarks from HTC One:

Algorithm (SecretKeyFactory instance): PBEWITHSHA1AND128BITAES-CBC-BC

Password (10 characters): thisisatest

Round 1

26400 - 502ms

26300 - 445ms

Round 2

27200 - 575ms

27100 - 441ms

Round 3

30000 - 510ms

29900 - 447ms



Benchmarks from Galaxy Nexus (thx @couga6442):

Algorithm (SecretKeyFactory instance): PBEWITHSHA1AND128BITAES-CBC-BC

Password (10 characters): thisisatest

Round 1

23100 - 614ms

23000 - 466ms

Round 2

19300 - 524ms

19200 - 396ms

Round 3

22700 - 506ms

22600 - 486ms



Benchmarks from HTC EVO Shift (thx @\VOH):

Algorithm (SecretKeyFactory instance): PBEWITHSHA1AND128BITAES-CBC-BC

Password (10 characters): thisisatest

Round 1

13800 - 767ms

13700 - 438ms

Round 2

17600 - 546ms

17500 - 496ms



Benchmarks from HTC EVO 4G (thx @couga6442):

Algorithm (SecretKeyFactory instance): PBEWITHSHA1AND128BITAES-CBC-BC

Password (10 characters): thisisatest

Round 1

13800 - 507ms

13700 - 491ms

Round 2

13700 - 509ms

13600 - 490ms



Great examples of proper implementations:
https://github.com/WhisperSystems/TextSecure/blob/master/src/org/thoughtcrime/securesms/crypto/MasterSecretUtil.java

https://github.com/nelenkov/android-pbe/blob/master/src/org/nick/androidpbe/MainActivity.java
http://nelenkov.blogspot.com/2012/04/using-password-based-encryption-on.html
