package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		try{
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(args[0]));
			JsonTranslator translator = gson.fromJson(reader,JsonTranslator.class);
			Ewoks.getInstance().createEwoks(translator.getEwoks());
			Thread leia = new Thread(new LeiaMicroservice(translator.getAttacks(),translator.getR2D2(),translator.getLando()));
			Thread c3po = new Thread(new C3POMicroservice());
			Thread han = new Thread(new HanSoloMicroservice());
			Thread r2d2 = new Thread(new R2D2Microservice());
			Thread lando = new Thread(new LandoMicroservice());
			leia.start();
			han.start();
			c3po.start();
			r2d2.start();
			lando.start();


			leia.join();
			han.join();
			r2d2.join();
			c3po.join();
			lando.join();
			Diary.getInstance().createOutputFile(args[1]);



		} catch (IOException | InterruptedException e){
			System.out.println("Fuck you");
		}

	}
}
