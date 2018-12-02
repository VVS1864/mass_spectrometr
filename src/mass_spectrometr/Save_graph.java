package mass_spectrometr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import javax.swing.JFileChooser;

import mass_spectrometr.Run.graph_type;
import mass_spectrometr.GUI.Save_dialog;

public class Save_graph {
	String big_string = "";

	public Save_graph(graph_type graph_type) {
		String g_name = "";
		if (graph_type == mass_spectrometr.Run.graph_type.MASS_GRAPH) g_name = "Mass";
		else if (graph_type == mass_spectrometr.Run.graph_type.ENERGY_GRAPH) g_name = "Energy";
		Save_dialog dialog = new Save_dialog(Run.prog.user_interface.mainFrame, "Save new " + g_name + " graph",
				JFileChooser.SAVE_DIALOG, Run.prog.save_directory, "graph_" + g_name +"_1.txt");
		if (dialog.userSelection == JFileChooser.APPROVE_OPTION) {
			// new current file name and dir
			Path p = Paths.get(dialog.selected_file);
			String f_name = p.getFileName().toString();
			Run.prog.save_directory = p.getParent().toString();

			get_big_string(graph_type);

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

	private void get_big_string(graph_type graph_type) {
		if (graph_type == mass_spectrometr.Run.graph_type.MASS_GRAPH) {
			for (int mass_1 = 0; mass_1 < Run.prog.fixed_data_mass_intensity.length; mass_1++) {

				double intensity_1 = Run.prog.fixed_data_mass_intensity[mass_1];
				if (intensity_1 == 0)
					continue;

				double x1 = mass_1;
				double y1 = intensity_1;

				put_to_big_string(x1, y1);
			}
		} else if (graph_type == mass_spectrometr.Run.graph_type.ENERGY_GRAPH) {
			for (int energy = 0; energy < Run.prog.fixed_data_en_el_intensity.length; energy++) {
				double intensity_1 = Run.prog.fixed_data_en_el_intensity[energy][0];

				if (Run.prog.fixed_data_en_el_intensity[energy][0] == 0)
					continue;

				if (Run.prog.fixed_data_en_el_intensity[energy][1] != 1) {
					intensity_1 = Run.prog.fixed_data_en_el_intensity[energy][0]
							/ (Run.prog.fixed_data_en_el_intensity[energy][1] - 1);
				}
				double en_el_1_float = Run.prog.calc_float_en_el(energy);

				double x1 = en_el_1_float;
				double y1 = intensity_1;

				put_to_big_string(x1, y1);

			}
		}
	}

	private void put_to_big_string(double x, double y) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		
		big_string += formatter.format(x);
		big_string += "          ";
		big_string += formatter.format(y);
		big_string += "\n";
	}
}
