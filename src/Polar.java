import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import processing.core.PApplet;
import processing.core.PFont;

public class Polar extends PApplet {

	static double pi = 3.14159265359;

	static int w = 1024; // window resolution
	static int h = 768;
	static int skip = 1;

	static float mx;
	static float my;

	static double k = 0.0; // starting multiplier of d(t); increases over time
	static int unit = 400; // number of pixels in one unit on the graph
	static double cycles = 1;

	static double k_step = 0.02;
	static int pause_state = 1;

	static int res = 1; // resolution of turtle plot

	static float translate_x = w / 2;
	static float translate_y = h - (h / 4);

	static double period;
	static float j0 = 1;

	static int num_periods = 0;

	static int turtle_line_weight = 2; // stroke weight of lines
	static String blend_mode = "mix"; // blending mode

	PFont f;

	static double status_message_duration = 100; // frames
	static DecimalFormat round;
	static DecimalFormat long_round;
	static DecimalFormat short_round;

	static boolean show_info_text = true;

	static int trace_quality = 60;
	static int line_height = 15;

	static float dash_length = 10; // length in pixels of graph unit marker
	static float dash_num = 4; // number of markers

	static double audio_trace_duration = 2.0; // seconds
	static int audio_sample_rate = 44100;
	static int audio_bit_depth = 16;
	static double audio_gain = 1.0;

	static ArrayList<float[]> bessel_plot = new ArrayList<>();
	static ArrayList<float[]> turtle_plot = new ArrayList<>();
	static boolean show_axes = true;
	static int trace_mode = 0;

	static boolean normalize = true;

	@Override
	public void settings() {
		size(w, h);

	}

	@Override
	public void setup() {
		frameRate(60);
		// smooth();
		rect(0, 0, width, height, 0);
		fill(0);
		blendMode(BLEND);
		// strokeCap(PROJECT);

		f = createFont("ProggyCleanTT", 12, true);

	}

	/////////////////////////////////////////////////////////////////////////////

	static float a = 3;

	static float r;
	static float scale = 50;
	static float[] last = new float[] { 0, 0 };
	static boolean background = false;
	static int opacity;
	static float line_weight;

	static double lower_bound = 0;
	static double upper_bound = 8*pi;

	static int resolution = 600; // fraction of circle drawn per frame

	static int steps_per_frame = 4;

	static double arc = 0.0;
	static double area = 0.0;

	static float theta = (float) lower_bound;

	static float x0;
	static float y0;
	static float x1;
	static float y1;

	@Override
	public void draw() {

		noStroke();
		// noFill();
		rect(0, 0, width, height);
		fill(0, opacity);

		int green = (int) (250 + 30 * sin(theta + 1));
		int blue = (int) (80 + 50 * sin(theta / 2));
		int red = 30;

		line_weight = 3 + 1 * cos(12 * theta);
		strokeWeight(line_weight);

		stroke(red, green, blue);
		translate(width / 2, height / 2);

		for (int i = 0; i < steps_per_frame; ++i) {

			theta += 2 * pi / resolution;

			r = (float) (Math.asin(sin(4*theta)));
			r *= scale;

			x1 = r * cos(theta);
			y1 = -r * sin(theta);

			if (i == 0 && arc == 0) {
				x0 = x1;
				y0 = y1;
			}
			if (theta <= (float) upper_bound) {

				line(x0, y0, x1, y1);

				arc += sqrt(pow(x1 - x0, 2) + pow(y1 - y0, 2)) / (double) scale;
				area += abs(x0 * y1 - y0 * x1) / 2;

				x0 = x1;
				y0 = y1;

			} else {

				// line(x0, y0, x1, y1);

				// System.out.println("area = " + area / scale);
				// System.out.println("arc length ~= " + arc);
				break;

			}
		}

		opacity = 3;

		// noFill();
		// showText();

	}

	/////////////////////////////////////////////////////////////////////////////

	private void showText() {

		translate(-width / 2, -height / 2);

		textFont(f, 16);

		String[] strings = new String[] {

				"line weight: " + line_weight,

				"r = " + r,

				"theta = " + theta

		};

		for (int i = 0; i < strings.length; ++i) {
			fill(12);
			text(strings[i], 6, line_height * (i + 1));
		}

	}

	@Override
	public void mouseDragged() {
		translate_x += mouseX - pmouseX;
		translate_y += mouseY - pmouseY;
	}

	@Override
	public void mouseMoved() {
		mx = mouseX;
		my = mouseY;
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		short_round = new DecimalFormat("0.00");
		round = new DecimalFormat("0.0000");
		long_round = new DecimalFormat("0.0000000");
		Polar mySketch = new Polar();
		PApplet.runSketch(processingArgs, mySketch);

	}
}
