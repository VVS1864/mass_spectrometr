package mass_spectrometr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import mass_spectrometr.GUI.Save_dialog;

public class Save_graph {
	String big_string = "";

	public Save_graph() {
		Save_dialog dialog = new Save_dialog(Run.prog.user_interface.mainFrame, "Save new graph",
				JFileChooser.SAVE_DIALOG, Run.prog.save_directory, "graph_1.txt");
		if (dialog.userSelection == JFileChooser.APPROVE_OPTION) {
			// new current file name and dir
			Path p = Paths.get(dialog.selected_file);
			String f_name = p.getFileName().toString();
			Run.prog.save_directory = p.getParent().toString();

			get_big_string();

			try {
				Files.deleteIfExists(p);
				p = Files.createFile(p);
			} catch (IOException ex) {
				System.out.println("Error creating file");
			}

			try (BufferedWriter writer = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
				writer.append(big_string);
				writer.flush();

			} catch (IOException exception) {
				System.out.println("Error writing to file");
			}

			System.out.println("Save as file: " + dialog.selected_file);
		}

	}

	private void get_big_string() {
		for (int mass_1 = 0; mass_1 < Run.prog.fixed_data_mass_intensity.length; mass_1++) {

			double intensity_1 = Run.prog.fixed_data_mass_intensity[mass_1];
			if (intensity_1 == 0)
				continue;

			double x1 = mass_1;
			double y1 = intensity_1;

			// (int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int)
			// Math.round(y2);
			big_string += Integer.toString((int) Math.round(x1));
			big_string += "          ";
			big_string += Integer.toString((int) Math.round(y1));
			big_string += "\n";

		}
	}
}
