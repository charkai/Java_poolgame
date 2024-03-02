import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class ConfigReader {

	private ArrayList<Ball> balls;
	private PoolTable table;

	private Ball whiteBall;
	public ConfigReader() {
		this.balls = new ArrayList<Ball>();
		this.table = null;
		this.whiteBall = null;
	}

	/**
	 * This function parses the file and uses the factory method to create the table and the balls in the game
	 * @param path The path of the json file to read
	 */

	public void parse(String path) {

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));

			// convert Object to JSONObject
			JSONObject jsonObject = (JSONObject) object;

			// reading the Table section:
			JSONObject jsonTable = (JSONObject) jsonObject.get("Table");

			// reading a value from the table section
			String tableColour = (String) jsonTable.get("colour");

			// reading a coordinate from the nested section within the table
			// note that the table x and y are of type Long (i.e. they are integers)
			Long tableX = (Long) ((JSONObject) jsonTable.get("size")).get("x");
			Long tableY = (Long) ((JSONObject) jsonTable.get("size")).get("y");

			// getting the friction level.
			// This is a double which should affect the rate at which the balls slow down
			Double tableFriction = (Double) jsonTable.get("friction");

			if (tableX <= 0 || tableY <= 0 || tableFriction <= 0) {
				// We need the table x and table y to be positive and non-zero
				// We also need the friction to be positive and non-zero (the balls would never stop if the friction was 0)
				throw new InvalidConfigFileException("Table was not initialised correctly");
			}


			// FACTORY PATTERN is used to create the table here
			GameEntityCreator creator = new PoolTableCreator(tableColour, tableX, tableY, tableFriction);
			GameEntity table = creator.create();
			PoolTable gameTable = (PoolTable) table;
			this.table = gameTable;


			// reading the "Balls" section:
			JSONObject jsonBalls = (JSONObject) jsonObject.get("Balls");

			// reading the "Balls: ball" array:
			JSONArray jsonBallsBall = (JSONArray) jsonBalls.get("ball");

			// reading from the array:
			for (Object obj : jsonBallsBall) {
				JSONObject jsonBall = (JSONObject) obj;

				// the ball colour is a String
				String colour = (String) jsonBall.get("colour");

				// the ball position, velocity, mass are all doubles
				Double positionX = (Double) ((JSONObject) jsonBall.get("position")).get("x");

				Double positionY = (Double) ((JSONObject) jsonBall.get("position")).get("y");

				Double velocityX = (Double) ((JSONObject) jsonBall.get("velocity")).get("x");
				Double velocityY = (Double) ((JSONObject) jsonBall.get("velocity")).get("y");

				Double mass = (Double) jsonBall.get("mass");

				// IF the position of the balls it outside of the pool table, throw an invalid config file exception
				// NOTE, as ball diameter is a constant of 25, this is used to determine whether the pool ball will be off the table

				if (positionX <= 0 || positionX + 25 >= tableX || positionY <= 0 || positionY+25 >= tableY) {
					throw new InvalidConfigFileException("Balls were not initialised correctly in file, please give valid inputs");
				}

				// IF THE BALLS ARE INITIALISED INSIDE the holes of the pool table, throw an invalid config file exception
				if (positionY >= 0 && positionY <= 30) {
					if (positionX <= 30 && positionX >= 0) {
						throw new InvalidConfigFileException("Ball was initialised inside the top left pocket of the pool table");
					}
					if (positionX <= tableX/2-15 && positionX >= tableX/2+15) {
						throw new InvalidConfigFileException("Ball was initialised inside top middle pocket of pool table");
					}
					if (positionX <= tableX-30 && positionX >= tableX) {
						throw new InvalidConfigFileException("Ball was intialised in top right pocket of pool tabke");
					}
				}

				if (positionY >= tableY-30 && positionY <= tableY) {
					if (positionX <= 30 && positionX >= 0) {
						throw new InvalidConfigFileException("Ball was initialised inside the top left pocket of the pool table");
					}
					if (positionX <= tableX/2-15 && positionX >= tableX/2+15) {
						throw new InvalidConfigFileException("Ball was initialised inside top middle pocket of pool table");
					}
					if (positionX <= tableX-30 && positionX >= tableX) {
						throw new InvalidConfigFileException("Ball was intialised in top right pocket of pool tabke");
					}
				}


				// FACTORY PATTERN is used here to create the balls
				GameEntityCreator bcreator = new BallCreator(colour, positionX, positionY, velocityX, velocityY, mass);
				GameEntity ball = bcreator.create();
				Ball poolBall = (Ball) ball;

				if (colour.equals("white")) {
					this.whiteBall = poolBall;
				}
				else {
					balls.add(poolBall);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidConfigFileException e) {
			System.out.println(e);
			System.exit(0);

		}
	}

	public PoolTable getTable() {
		return table;
	}

	public ArrayList<Ball> getBalls() {
		return balls;
	}

	public Ball getWhiteBall() {
		return this.whiteBall;
	}


}
