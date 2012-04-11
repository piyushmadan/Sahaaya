/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sahaaya;

/**
 *
 * @author piyush
 */

import processing.core.*;
import hypermedia.video.OpenCV;



public class OpenCV_PApplet extends PApplet {


	OpenCV cv = null;	// OpenCV object


	/**
	 * Initialise Objects.
	 */
	public void setup() {

		// PApplet setup
		size( 640, 480 );
		background(0);

		// OpenCV setup
		cv = new OpenCV( this );
		cv.capture( width, height );
	}


	/**
	 * Display the input video stream in invert mode.
	 */
	public void draw() {
		cv.read();
		cv.invert();
		image( cv.image(), 0, 0 );
	}


	/**
	 * Call the PApplet main method.
	 */
	public static void main( String[] args ) {
		System.out.println( "\nOpenCV meet Processing PApplet\n" );
		PApplet.main( new String[]{"OpenCV_PApplet"} );
	}

}
