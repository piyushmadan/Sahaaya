/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sahaaya;

/**
 *
 * @author piyush
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import hypermedia.video.OpenCV;


//To create and use image object (buffered images from webcam)
import java.io.*;
import sun.audio.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.imageio.IIOImage;
import java.awt.image.BufferedImage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Image.*;
import sahaaya.MouseControl;
import sahaaya.Gesture;

 // to use absolute difference of prev & current coordinate points
import java.lang.Math.*;


public class FaceDetection extends Frame implements Runnable {
	/**
	 * Main method.
	 * @param String[]	a list of user's arguments passed from the console to this program
	 */

    public static int xCoord=0,yCoord=0,xCoord_ref=0,yCoord_ref=0,first_time=0;
    public static int prevxv=0,prevyv=0,prevxyFirst=0;
    public static int prevRed=0,prevBlue=0,prevGreen=0;

    static public int gesture_parameter=0, gesture_down=0,gesture_up=0;

// program execution frame rate (millisecond)
    final int FRAME_RATE  = 100/30;


    OpenCV cv = null;	// OpenCV Object
    Thread t  = null;	// the sample thread



// the input video stream image
    Image frame	= null;

// list of all face detected area
    Rectangle[] squares = new Rectangle[0];

	/**
	 * Setup Frame and Object(s).
	 */
public  FaceDetection() {

// OpenCV setup
    cv = new OpenCV();
    cv.capture( 320, 240 );
    cv.cascade( OpenCV.CASCADE_FRONTALFACE_ALT );

// frame setup
    this.setBounds( 100, 100, cv.width, cv.height );
    this.setBackground( Color.BLACK );
    this.setVisible( true );
    this.addKeyListener(
	new KeyAdapter() {
		public void keyReleased( KeyEvent e )
                        {
                            // ESC : release OpenCV resources
            			if ( e.getKeyCode()==KeyEvent.VK_ESCAPE )
                                {
                                	cv.dispose();
					System.exit(0);
				}
                        }
                                    }
                            );

		// start running program
		t = new Thread( this );
		t.start();
	}
	/**
	 * Draw video frame and each detected faces area.
	 */
public void paint( Graphics g ) {

    Image image = null;
    // Read from a file
  try {
       if(first_time==0)
       {
//      File file = new File("/home/piyush/NetBeansProjects/sahaaya/src/sahaaya/test.jpeg");
//      image = ImageIO.read(file);
//      g.drawImage(image, 100, 100, this);
        }
       }
  catch(Exception e)
  {
      System.out.print(e);
  }

// draw image
    g.drawImage( frame, 0, 0, null );

// draw squares
	g.setColor( Color.blue );
	for( Rectangle rect : squares )
                {
                   g.drawRect(rect.x, rect.y, rect.width, rect.height);
                   g.setColor(Color.ORANGE);
                   g.drawLine(xCoord_ref-10,  yCoord_ref-50, xCoord_ref-10,  yCoord_ref+50);
                   g.drawLine(xCoord_ref+10,  yCoord_ref-50, xCoord_ref+10,  yCoord_ref+50);
                   g.setColor(Color.pink);
                   g.drawLine(xCoord_ref-50,  yCoord_ref+10, xCoord_ref+50,  yCoord_ref+10);
                   g.drawLine(xCoord_ref-50,  yCoord_ref-10, xCoord_ref+50,  yCoord_ref-10);

                   g.setColor( Color.red );

 //Following Messages can be customised in upcoming versions
                  g.drawString("Not Feeling well",xCoord_ref+25,  yCoord_ref+25 );
                  g.drawString("Feeling",xCoord_ref-75,  yCoord_ref-25 );
                  g.drawString("Hungry",xCoord_ref-75,  yCoord_ref-15 );
                  g.drawString("Feeling Suffocated",xCoord_ref+25,  yCoord_ref-25 );
                  g.drawString("Need Medical Attention",xCoord_ref-150,  yCoord_ref+25 );
                  g.drawString("Nature's Call",xCoord_ref+25,  yCoord_ref );
                  g.drawString("Any Message",xCoord_ref-95,  yCoord_ref  );

                  g.setColor( Color.blue );

             if(  first_time==0 && rect.x!=0 && rect.y!=0 && rect.width!=0 && rect.height!=0)
             {
         //    xCoord_ref=xCoord;
         //    yCoord_ref=yCoord;
               xCoord_ref=(rect.x) + ((rect.width) / 2);
               yCoord_ref=(rect.y) + ((rect.height)/2);
               System.out.println("FIRST TIME!!!:: Coordinates set:"+xCoord_ref+","+yCoord_ref);
               first_time=1;
             }
                    
             if(first_time!=0 )
             {
//            ModifiedMoveMouse_With_Time_Control(rect.x+ ((rect.width) / 2), rect.y + ((rect.height)/2),xCoord_ref,yCoord_ref);
//            ModifiedMoveMouse(rect.x+ ((rect.width) / 2), rect.y + ((rect.height)/2),xCoord_ref,yCoord_ref);
//            moveMouse((rect.x) + ((rect.width) / 2), (rect.y) + ((rect.height) / 2));

//To check the head movement in vertical axis
            Gesture_y((rect.y + ((rect.height)/2)));

//Gesture ge = new Gesture.Gesture_y((rect.y + ((rect.height)/2)));

                g.setColor(Color.red);
                g.setColor(Color.YELLOW);
                g.drawOval(rect.x + (rect.width) / 2, rect.y + ((rect.height)/2), 10, 10);
                g.setColor(Color.GREEN);
                g.drawOval(xCoord_ref,  yCoord_ref, 10, 10);

                System.out.println("Coordinates set:"+rect.x+","+rect.y+"     width="+rect.width+"  height="+rect.height);
                //   g.drawString("o", xCoord_ref,  yCoord_ref);
                //   g.drawOval(xCoord_ref,  yCoord_ref, 10, 10);

//Gesture Related parameters

                 prevxv=rect.x + (rect.width) / 2;
                 prevyv=(rect.y + ((rect.height)/2));
                 prevxyFirst=1;
             }
           }
        }
	/**
         *
	 * run method
	 */
	public void run() {
		while( t!=null && cv!=null ) {
			try {
				t.sleep( FRAME_RATE );

				// grab image from video stream
				cv.read();

				// create a new image from cv pixels data
				MemoryImageSource mis = new MemoryImageSource( cv.width, cv.height, cv.pixels(), 0, cv.width );
				frame = createImage( mis );

				// detect faces
				squares = cv.detect( 1.2f, 2, OpenCV.HAAR_DO_CANNY_PRUNING, 20, 20 );


				// of course, repaint
				repaint();
			}
			catch( InterruptedException e ) {;}
		}
	}


public static long  Time_mouse_moved=0;

public void ModifiedMoveMouse_With_Time_Control(int x , int y, int xCoord_ref , int yCoord_ref)
    {
        long Time_now_move_mouse=System.currentTimeMillis();
        long difference_time=  Time_now_move_mouse  -  Time_mouse_moved;

       if(difference_time>500)
            {
             System.out.println("difference_time is >100");
              int Screen_width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
              int Screen_height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

                    try{
                       Robot robot1 = new Robot();
                       int centre_screen_x= ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width) /2);
                       int centre_screen_y= ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().height) /2);
                       int dx = xCoord_ref-x;
                       int dy = yCoord_ref-y;
                       System.out.println("dx="+dx+",dy="+dy);

                       int x_Scaling_Factor=40;
                       int y_Scaling_Factor=40;

                       dx=dx*x_Scaling_Factor; //Scale the difference to size of screen
                       dy=dy*y_Scaling_Factor;

                       robot1.mouseMove((centre_screen_x+dx),Screen_height-(centre_screen_y+dy));

                    }
                      catch (AWTException e) {
                       System.out.println(e);
                                }
        }
    }

public static int prevMouseCoordinateX;
public static int prevMouseCoordinateY;
public static int PresentMouseCoordinateX;
public static int PresentMouseCoordinateY;

public void ModifiedMoveMouse(int x , int y, int xCoord_ref , int yCoord_ref)
    {

       int Screen_width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
       int Screen_height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

       try{
            Robot robot1 = new Robot();
            int centre_screen_x= ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width) /2);
            int centre_screen_y= ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().height) /2);
            int dx = xCoord_ref-x;
            int dy = yCoord_ref-y;
            System.out.println("dx="+dx+",dy="+dy);


             int x_Scaling_Factor=20;
             int y_Scaling_Factor=20;
             dx=dx*x_Scaling_Factor; //Scale the difference to size of screen
             dy=dy*y_Scaling_Factor;

             PresentMouseCoordinateX=(centre_screen_x+dx);
             PresentMouseCoordinateY=Screen_height-(centre_screen_y+dy);

       //         VERY IMPORTANT COMMENT... removed on 27 March to test smooth transition
       //        robot1.mouseMove((centre_screen_x+dx),Screen_height-(centre_screen_y+dy));

             MouseSmoothTransition(PresentMouseCoordinateX,PresentMouseCoordinateY, prevMouseCoordinateX, prevMouseCoordinateY)  ;

             prevMouseCoordinateX=PresentMouseCoordinateX;
             prevMouseCoordinateY =PresentMouseCoordinateY;
           }
          catch (AWTException e) {
              System.out.println(e);
                               }
    }


void  MouseSmoothTransition(int x0,int y0, int x1, int y1) throws AWTException {

        Robot r = new Robot();

        System.out.println("================Trying Mouse Smooth Transition===============") ;

//		int x0 = 0;
//		int y0 = 0;
//		int x1 = 1280;
//		int y1 = 1024;

		int dx = x1 - x0;
		int dy = y1 - y0;
                // time in msecs
		int time = 10;
		int res = Math.max(dx, dy);

		if(res > time) res = time;
		int d = time/res;

		float inv = (float) 1/(res - 1);
		float a = 0;

		long s = 0;
		long e = 0;
		s = System.currentTimeMillis();
		for(int i = 0; i < res; i++) {
			a += inv;
			r.mouseMove(x0 + (int) (a*dx), y0 + (int) (a*dy));
			r.delay(d);
		}
		e = System.currentTimeMillis();
		System.out.println("Total time: " + (float) (e - s)/1000);

	}

public void moveMouse(int xv,int yv)  //xCoord & xv are same....
    {

    int Screen_width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    int Screen_height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

    System.out.println(Screen_width+","+Screen_height);
    long Time_now_move_mouse=System.currentTimeMillis();
    long difference_time=  Time_now_move_mouse  -  Time_mouse_moved;

    if(difference_time>200)
            {
             System.out.println("difference_time is >100");


            int gm,gr,xwmin=0,ywmin=0,xwmax=255,ywmax=255,xvmin=0,yvmin=0,xvmax=Screen_width,yvmax=Screen_height,xw=xCoord,yw=yCoord,sx,sy;
//  int xv,yv;
//System.out.println(Screen_width+"      "+Screen_height+"       _________");

//            xwmax=xCoord_ref+50;
//            ywmax=yCoord_ref+30;
//            xwmin=xCoord_ref-50;
//            ywmin=yCoord_ref-30;

//            sx= ((xvmax-xvmin)/(xwmax-xwmin));
//            sy=(yvmax-yvmin)/(ywmax-ywmin);
//            xwmax=xCoord_ref+50;
//            ywmax=yCoord_ref+30;
//            xwmin=xCoord_ref-50;
//            ywmin=yCoord_ref-30;

//            xv=(sx*(xw-xwmin))+xvmin;
//            yv=(sy*(yw-ywmin))+yvmin;


//Modified sx (due to rescrition in head movement
//          sx= ((xvmax-xvmin)/(xwmax-xwmin))

// Removed on 14 March 2012
//            xCoord= xCoord - (xCoord_ref - 50);
//            xCoord= yCoord - (yCoord_ref - 30);
//            xCoord = xCoord*21;
//            yCoord = yCoord*8;

//REMOVE BELOW.... REPEATED TO CHECK SENSITIVITY
//            xCoord = xCoord*13;
//            yCoord = yCoord*13;

      try {
//   These coordinates are screen coordinates
//                      xv= xCoord ;
//                      yv= yCoord;

//    The coordinates should not go out of screen
                     if(xCoord>=Screen_width)
                     {
                         xCoord=Screen_width;
                     }

                     if(yCoord>=Screen_height)
                     {
                         yCoord=Screen_height;
                     }

//Move mouse only if leap is not very large
//abs - for absolute under java.lang.math not working
//                   if(prevxyFirst!=0)
//                   {
//                        if( ((prevxv-xv)>50) || ((prevxv-xv)<-50)|| ((prevyv-yv)>30)|| ((prevyv-yv)<-30) )
//                         {
//                                System.out.println("Big Jump");
//                         }
//                   }
// Move the cursor

      Robot robot = new Robot();
      int ScreenxCoord= Screen_width-xCoord;
      robot.mouseMove(Screen_width-xCoord,yCoord);

      System.out.print("Cursor on screen=("+ScreenxCoord+","+yCoord+") & Reference point= ("+xCoord_ref+","+yCoord_ref+") & xv, yv->"+xv+","+yv);
      System.out.println();
      Time_mouse_moved=System.currentTimeMillis();

//Important recordings for Gestures
//       Gesture(yv);

//     Important links for mouse control
//     http://download.oracle.com/javase/6/docs/api/java/awt/event/InputEvent.html
 //    http://undocumentedmatlab.com/blog/gui-automation-robot/
//void mousePress(int buttons)
////void mouseRelease(int buttons)
////This pair of functions performs the button click. Their input argument is an OR’ed combination of java.awt.event.InputEvents:

////java.awt.event.InputEvent.BUTTON1_MASK   // left mouse button
////java.awt.event.InputEvent.BUTTON2_MASK   // middle mouse button
////java.awt.event.InputEvent.BUTTON3_MASK   // right mouse button

                       }
        catch (AWTException e) {
                               }
           }
}




//Sensing Gestures in vertical axis
public void Gesture_y (int yv)
    {
        System.out.println("Gesture Called");

        if(gesture_parameter==0)
        {
             System.out.println("Gesture parameter is ZERO");
           // if (prevyv > yv) //Downward movement
             if(prevyv - yv > 2)
                {
                    gesture_down++;
                    System.out.print("---   down  --- ");
                }
            else
                {
                    gesture_down=0;
                }

              if(gesture_down>3)    //
                    {
                    gesture_parameter=1;
                    System.out.println("Gesture Parameter Set to TWO");
                    System.out.println("DOWNWARD MOVEMENT DETECTED");
                    }
        }

        if(gesture_parameter==1)
            if(prevyv - yv <2)   //Upward Movement after downward movement
        {
            if(prevyv - yv < 2 )
                {
                     gesture_up++;
                     System.out.print("--------------   up  ------------------- ");
                }
            else
                {
                    gesture_up=0;
                    gesture_parameter=0;
                }

             if(gesture_up>3)
             {
                 System.out.println("UPWARD MOVEMENT DETECTED");
                 gesture_parameter=2;
                 System.out.println("Gesture Parameter Set to TWO");
             }

        }
          if(gesture_parameter==2)
              if(prevyv>yv)     //SEC TIME UPWARD MOVEMENT
          {
              if (prevyv > yv) //Downward movement
                {
                    gesture_down++;
                    System.out.println("-------------------------   down  ----------------------------- ");
                }
                else
                {
                    gesture_down=0;
                    gesture_parameter=0;
                    System.out.println("Gesture Parameter Set to ZERO");
                }

              if(gesture_down>3)    //
                    {
                    gesture_parameter=0;
                        System.out.println("Gesture Parameter Set to ZERO AFTER SUCCESS");
                    System.out.println("DOWNWARD MOVEMENT DETECTED");

                    System.out.println("..************.............GESTURE SUCCESSFUL.................**********..");

                    try{
                        //AUDIO BEEP IS GENERATED
                        String Filename= "/home/piyush/Music/Beep.wav";
                        InputStream in = new FileInputStream(Filename);
                        // Create an AudioStream object from the input stream.
           
                        AudioStream as = new AudioStream(in);
                        System.out.println("22");

// Use the static class member "player" from class AudioPlayer to play
                        AudioPlayer.player.start(as);
// Similarly, to stop the audio.
//AudioPlayer.player.stop(as);
                     }
             catch(Exception ex)
                     {
                 System.out.println(ex);
                     }



    }

}

//   gesture_parameter=0;
//   gesture_down=0;
//   gesture_up=0;

}

}
