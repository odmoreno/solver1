package com.amazonaws.lambda.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class LambdaFunctionHandler implements RequestHandler<Map<String, Object>, String> {

	@Override
    public String handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Input: " + input);

        String msg= "";
        
        @SuppressWarnings("deprecation")
		AmazonS3Client s3Client = new AmazonS3Client();
        
        
        String bucket = (String) input.get("bucket");
        String key = (String) input.get("key");
        
        ArrayList<UtilityFunction> functions = new ArrayList<UtilityFunction>();
        ArrayList<?> mrc, point, alto;
        double x2;
        int x1;
        int total = 0, totalCurvas = 0, alto1=0;
        
        App al;
        try {
        	//PrintWriter pw = new PrintWriter(new File("test.txt"));
        	//StringBuilder sb = new StringBuilder();
        	
        	int mem = (int)input.get("totalMemory");
        	double M = (double) mem;
            ArrayList<?> weights = (ArrayList<?>) input.get("weights");
            ArrayList<?> memory = (ArrayList<?>) input.get("minimumMemory");
            ArrayList<?> bdi = (ArrayList<?>) input.get("cdi");
            ArrayList<?> cdi = (ArrayList<?>) input.get("cdi");
            ArrayList<?> fq = (ArrayList<?>) input.get("frequency");

            
            double[] pesos = new double[weights.size()];
            double[] memoria = new double[memory.size()];
            double[] sizeMemory = new double[memory.size()];
            
            double[] _bdi = new double[bdi.size()];
            double[] _cdi = new double[cdi.size()];
            double[] _fq = new double[fq.size()];

            for (int i = 0; i < weights.size(); ++i) {
            	int x = (int)weights.get(i);
                double d = (double)x;
                pesos [i] =  d;
                //System.out.println(pesos[i]);
            }
            for (int i = 0; i < memory.size(); ++i) {
            	int x = (int)memory.get(i);
                double d = (double)x;
                memoria [i] =  d;
                //System.out.println(memoria[i]);
            }
            for (int i = 0; i < fq.size(); ++i) {
                int x = (int)fq.get(i);
            	double d = (double)x;
                _fq [i] =  d;
                //System.out.println(_fq[i]);
            }
            
            for (int i = 0; i < bdi.size(); ++i) {
            	if (bdi.get(i) instanceof Integer) {
            		int x = (int)bdi.get(i);
                    double d = (double)x;
                    _bdi [i] =  d;
                   // System.out.println(_bdi[i]);
            	}
            	else {
            		double d = (double)bdi.get(i);
                    _bdi [i] =  d;
            	}
            }
            
            for (int i = 0; i < cdi.size(); ++i) {
            	if (cdi.get(i) instanceof Integer) {
            		int x = (int)cdi.get(i);
                    double d = (double)x;
                    _cdi [i] =  d;
                    //System.out.println(_cdi[i]);
            	}
            	else {
            		double d = (double)cdi.get(i);
                    _cdi [i] =  d;
            	}
            }
           
            
            ArrayList<?> list = (ArrayList<?>)  input.get("mrc");
            totalCurvas = (int) list.size();
            
            if (list != null) {
                int len = list.size();
                for (int i=0; i<len; i++) {
                    //System.out.println(" " + list.get(i));
                    mrc = (ArrayList<?>) list.get(i);
                    UtilityFunction f = new UtilityFunction();
                    alto = (ArrayList<?>) mrc.get(mrc.size()-1);
                    total += (int) mrc.size();
                    
                    alto1 = (int)(alto.get(0));
                    double d1 = (double) alto1;
                    sizeMemory[i] = d1;
                    
                    for (int z=0; z < mrc.size(); z++){
                        //System.out.println(" " + mrc.get(z));
                        point =  (ArrayList<?>) mrc.get(z);
                        if (point.get(1) instanceof Integer ) {
                        	int x = (int)point.get(1);
                        	double d = (double)x;
                        	x1 = (int) (point.get(0));
                        	x2 = 1 - d;

                        }
                        else {
                        	x1 = (int) (point.get(0));
                            x2 = 1 -(double) point.get(1);
                        }
                        f.addPoint((double)x1, x2);
                    }
                    functions.add(f);
                }
            }
            
            al = new App(4);
            long startTime = System.nanoTime();
            String resultado = "", datos = "";
            double[] result = al.probabilisticAdactiveSearch(total , totalCurvas,functions, pesos, 1000, M, memoria, _bdi, _cdi, _fq, sizeMemory);
            for (int i=0;i< result.length; i++ ) {
                //System.out.println(result[i]);
                if (i == result.length -1){
                    resultado += result[i];
                }
                else {
                    resultado += result[i] + ",";
                }
            }
            long endTime = System.nanoTime();
            double totalTime = endTime - startTime;
            double time = totalTime/ 1000000000.0;

            datos += time + "," + resultado;
            msg = datos;
            
            
            s3Client.putObject(bucket,key, resultado);
            s3Client.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
            //s3Client.putObject(
            //		   new PutObjectRequest(bucket, key, resultado).withCannedAcl(CannedAccessControlList.PublicRead));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       
        return msg;
    }

}
