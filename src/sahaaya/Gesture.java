/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sahaaya;

import java.io.*;
import sun.audio.*;
import sahaaya.Main;
import sahaaya.FaceDetection;

/**
 *
 * @author piyush
 */
public class Gesture extends FaceDetection {

    Gesture( int a)
    {
    Gesture_y(a);
    }
    }



   public void Gesture_ (int yv)
    {
        System.out.println("Gesture Called");

        if(gesture_parameter==0)
        {
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


                      if(gesture_down>5)    //
                    {
                    gesture_parameter=0;
                        System.out.println("Gesture Parameter Set to ZERO AFTER SUCCESS");
                    System.out.println("DOWNWARD MOVEMENT DETECTED");

                    System.out.println("..************.............GESTURE SUCCESSFUL.................**********..");
                    try{



            String Filename= "/home/piyush/Music/Beep.wav";
            InputStream in = new FileInputStream(Filename);
            // Create an AudioStream object from the input stream.
            System.out.println("20");

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

       //     gesture_parameter=0;
       //   gesture_down=0;
       //   gesture_up=0;

    }







}
