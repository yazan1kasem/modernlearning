package modern.learning.modernlearning;


import javafx.application.Platform;
import javafx.stage.Stage;
import modern.learning.modernlearning.CalenderClasses.KalenderPopover;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.AssertJUnit.*;

public class UnitTests {


    @BeforeTest
    public static void initJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @Test
    public void get_Currentuser() throws IOException {
        Platform.runLater(() -> {
            try {
                Starter starter = new Starter();
                starter.start(new Stage());
                assertEquals("yazan", Currentuser.getUsername());
            } catch (IOException e) {
                // Handle IOException if necessary
                e.printStackTrace();
            }
        });
    }



    @Test
    public void Insert_file() {
        // Create an instance of EigeneDatein
        Currentuser.setUsername("yazan");
        EigeneDatein eigeneDatein = new EigeneDatein();

        // Define the file path and name after saving
        String fileName = "1BMSV.pdf";
        String expectedFilePath = "src/main/EigeneDatein/" + fileName;

        // Create a mock file object with the specified name
        File mockFile = new File("src/main/Files/" + fileName);

        // Call the saveFile method
        eigeneDatein.SaveFile(mockFile);

        // Verify that the file was saved to the expected location
        Path savedFilePath = Paths.get(expectedFilePath);
        assertTrue( "File was not saved to the expected location: " + expectedFilePath, Files.exists(savedFilePath));
    }




}
