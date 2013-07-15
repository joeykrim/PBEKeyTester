package com.joeykrim.pbekeytester;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.spec.*;
import android.util.*;
import java.util.*;
import android.support.v4.util.*;
import java.math.*;

public class MainActivity extends Activity {

    String LOG_TAG = "TestIterations";
	
	//initialize previous iteration count, time and elapsed time
    long previousIterationCount = 0L, previousIterationTime = 0L, previousIterationElapsedTime = 0L;

    //set algorithm name
    String algorithName = "PBEWITHSHA1AND128BITAES-CBC-BC";

    //set generic password
    String passphrase = "thisisatest"; //10 characters long

    //set goal time in ms
    long goalTime = 500L;
    
    //use range for goal time
    //target 500ms using range +-20%
    long targetGoalMinimum = 450L;//goalTime*(long)0.8;
    long targetGoalMaximum = 550L;//goalTime*(long)1.2;

    //set iteration increment step
    //int iterationStep = 100; //need to determine best increment step

    //initialize iteration starting point
    //int currentIterationCount = 0;
    int keyIterationLocationStart = 10000;
    int keySmallIterationIncrement = keyIterationLocationStart/2; //5000
    int keyLargeIterationIncrement = keyIterationLocationStart*2; //20000
    int keyIterationCurrent = keyIterationLocationStart;
    int currentIteration = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        ((TextView) findViewById(R.id.resultsText)).setText("Solving...");

        backgroundIterationCount bTask = new backgroundIterationCount();
        bTask.execute();
    }

    public ArrayList<Long> solveTargetIteractionCount() {

        ArrayList<Long> results = new ArrayList<Long>();

        long overallStartTime = SystemClock.elapsedRealtime();

        while(true) {
            //currentIterationCount += iterationStep;

            //int startTime = System.currentTimeMillis();
            long startTime = SystemClock.elapsedRealtime();

            SecretKey sk = generateKey(passphrase, generateSalt(), keyIterationCurrent, algorithName);

            //int finishTime = System.currentTimeMillis();
            long finishTime = SystemClock.elapsedRealtime();

            long elapsedTime = finishTime-startTime;

            if ((currentIteration == 1 || currentIteration == 2) && elapsedTime < (targetGoalMinimum/2)) {
                //increase iteration count by larger increment
                Log.d(LOG_TAG, "Iteration count " + currentIteration + " at Key Iterations of " + keyIterationCurrent
                    + " took " + elapsedTime + " which is less than half the targetGoalMinimum by " + ((400L/2L)-elapsedTime)
                    + "ms taking overall time of " + (SystemClock.elapsedRealtime()-overallStartTime) + "ms");
				keyIterationCurrent += keyLargeIterationIncrement;
            } else if (elapsedTime < targetGoalMinimum) {
                //increase iteration count by smaller increment
                Log.d(LOG_TAG, "Iteration count " + currentIteration + " at Key Iterations of " + keyIterationCurrent
                    + " took " + elapsedTime + " which is less than the targetGoalMinimum by " + (targetGoalMinimum-elapsedTime)
                    + "ms taking overall time of " + (SystemClock.elapsedRealtime()-overallStartTime) + "ms");
				keyIterationCurrent += keySmallIterationIncrement;
            } else if (elapsedTime > targetGoalMaximum) {
                //decrease iteration count
			    Log.d(LOG_TAG, "Iteration count " + currentIteration + " at Key Iterations of " + keyIterationCurrent
                    + " took " + elapsedTime + " which is greater than the targetGoalMaximum by " + (targetGoalMaximum-elapsedTime)
                    + "ms taking overall time of " + (SystemClock.elapsedRealtime()-overallStartTime) + "ms");
				keyIterationCurrent -= keySmallIterationIncrement;
            } else {
            	
            //if (elapsedTime > goalTime) {
                Log.d(LOG_TAG, "Iteration count " + currentIteration + " at Key Iterations of " + keyIterationCurrent
                    + " took " + elapsedTime + " which is between the targetGoalMinimum of " + targetGoalMinimum
                    + "ms and the targetGoalMaximum of " + targetGoalMaximum
                    + "taking a total search time of " + (SystemClock.elapsedRealtime()-overallStartTime) + "ms");
                //Log.d(LOG_TAG, "The previous iteration count was: "
                    //+ (currentIterationCount - iterationStep) + " and took: " + previousIterationElapsedTime + "ms that is under the goalTime of " + goalTime + " by " + (goalTime-previousIterationElapsedTime) + "ms");

                //Output key
                //http://stackoverflow.com/a/7176483
                Log.d(LOG_TAG, "Final SecretKey: " + new BigInteger(1, sk.getEncoded()).toString(16));

                results.add(finishTime - overallStartTime); //overall time consumed
                results.add( (long) keyIterationCurrent); //targetIterationCount
                results.add(elapsedTime); //targetIterationTime
                results.add(0L);
                results.add(0L);
                //results.add( (long) currentIterationCount - iterationStep); //previousIterationCount
                //results.add(previousIterationElapsedTime); //previousIterationTime
                //break;
                return results;
            //} else {
                //Log.d(LOG_TAG, "Current iteration count of " + currentIterationCount + " took " + (finishTime-startTime) + "ms and has " + (goalTime-elapsedTime) + "ms more to reach the goalTime of: " + goalTime + "ms");
                //previousIterationElapsedTime = elapsedTime;
                //if (elapsedTime < ((goalTime/4)*3)) iterationStep *= 2;
                //else iterationStep = 700;
            }
			currentIteration++;
        }
    }


    //https://github.com/WhisperSystems/TextSecure/blob/master/src/org/thoughtcrime/securesms/crypto/MasterSecretUtil.java#L233
    //notes, this changed in android 4+? to SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
    public byte[] generateSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            //textsecure uses 8, NIST recommends minimum 16
            random.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "NoSuchAlgorithmException: " + e.toString());
        }
        return salt;
    }

    //https://github.com/WhisperSystems/TextSecure/blob/master/src/org/thoughtcrime/securesms/crypto/MasterSecretUtil.java#L241
    public SecretKey generateKey(String passphrase, byte[] salt, int currentIterationCount, String algorithName) {
        SecretKey sk = null;
        try {
            PBEKeySpec keyspec = new PBEKeySpec(passphrase.toCharArray(), salt, currentIterationCount);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithName);
            sk = skf.generateSecret(keyspec);
        } catch (InvalidKeySpecException e) {
            Log.e(LOG_TAG, "InvalidKeySpecException: " + e.toString());
        } catch (NoSuchAlgorithmException e2){
            Log.e(LOG_TAG, "NoSuchAlgorithmException: " + e2.toString());
        }
        return sk;
    }

    public class backgroundIterationCount extends AsyncTask<Void, Void, ArrayList<Long>> {
        @Override protected ArrayList<Long> doInBackground(Void... params) {
            return solveTargetIteractionCount();
        }

        @Override protected void onPostExecute(ArrayList<Long> results) {
            if (!isCancelled()) {
                ((TextView) findViewById(R.id.resultsText)).setText("The below results are using the algorithm: "+ algorithName
                    + " with passphrase: " + passphrase + System.getProperty("line.separator")
                    + System.getProperty("line.separator") + "Overall time required: " + results.get(0) + "ms"
                    + System.getProperty("line.separator") + "Target Iteration Count: " + results.get(1)
                    + System.getProperty("line.separator") + "Target Iteration Duration: " + results.get(2) + "ms"
                    + System.getProperty("line.separator") + System.getProperty("line.separator")
                    + "Prior Iteration Count: " + results.get(3) + System.getProperty("line.separator")
                    + "Prior Iteration Duration: " + results.get(4) + "ms");
            }
        }
    }
	
}
