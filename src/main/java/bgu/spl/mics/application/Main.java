package bgu.spl.mics.application;

import bgu.spl.mics.Test;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(Test test) {
		System.out.println("----Program Starts----");
		try{
//			Gson gson = new Gson();
//			JsonReader reader = new JsonReader(new FileReader(args[0]));
//			JsonTranslator translator = gson.fromJson(reader,JsonTranslator.class);

			Ewoks.getInstance().createEwoks(test.getEwoks());

			CountDownLatch initializeDoneSignal = new CountDownLatch(4);
			CountDownLatch terminateDoneSignal = new CountDownLatch(5);


			Thread leia = new Thread(new LeiaMicroservice(test.getAttacks(),test.getR2D2(),test.getLando(),initializeDoneSignal,terminateDoneSignal));
			Thread c3po = new Thread(new C3POMicroservice(initializeDoneSignal,terminateDoneSignal));
			Thread han = new Thread(new HanSoloMicroservice(initializeDoneSignal,terminateDoneSignal));
			Thread r2d2 = new Thread(new R2D2Microservice(initializeDoneSignal,terminateDoneSignal));
			Thread lando = new Thread(new LandoMicroservice(initializeDoneSignal,terminateDoneSignal));

			leia.start();
			han.start();
			c3po.start();
			r2d2.start();
			lando.start();

//			leia.join();
//			han.join();
//			r2d2.join();
//			c3po.join();
//			lando.join();

			terminateDoneSignal.await();
			Diary.getInstance().createOutputFile("Output.json");

		} catch (Exception  e){
			e.printStackTrace();
		}
	}
}
