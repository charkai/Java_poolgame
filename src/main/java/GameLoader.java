import javafx.application.Application;

import javafx.stage.Stage;

import java.util.List;

import java.util.ArrayList;

public class GameLoader extends Application {

    // This class launches the application
    @Override
    public void start(Stage primaryStage) {


        ConfigReader config = setConfig();

        // This class loads the game- creating a new game and receiving all of the information from the config file
        PoolGameManager pool = new PoolGameManager(config.getTable().getTableX(), config.getTable().getTableY());
        pool.setTable(config.getTable());
        pool.setBalls(config.getBalls());
        pool.setWhiteBall(config.getWhiteBall());
        pool.setInitialState((ArrayList<Ball>) config.getBalls().clone());

        primaryStage.setScene(pool.getScene());
        primaryStage.setTitle("Pool Game");
        primaryStage.show();

        pool.run();

    }
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * This function can take in the location of the configPath via the command line, and set up a configuration based on this.
     * If not given a commandline argument, it defaults to the default config.json file.
     * @return A config reader object that contains all of the information about the Configuring JSON file
     */
    public ConfigReader setConfig() {
        Parameters params = getParameters();
        List<String> parameters = params.getRaw();
        String configPath;
        if (parameters.size() > 0) {
            configPath = parameters.get(0);
        }
        else {
            configPath = "src/main/resources/config.json";
        }
        ConfigReader config = new ConfigReader();
        config.parse(configPath);

        return config;
    }
}