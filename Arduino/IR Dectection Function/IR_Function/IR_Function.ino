#include <SharpIR.h>
#include <time.h>

/*ProtoThreading file
 * Primary file for teh arduino program to control all periphals
 * Written by group
 * 
 */ //Pt library used for protothreading
 
#include <pt.h>


/* Model : Model numbers for different Sharp sensors needed to config the IR sensor
  GP2Y0A02YK0F --> 20150
  GP2Y0A21YK0F --> 1080
  GP2Y0A710K0F --> 100500
  GP2YA41SK0F --> 430
*/

// Definitions for all variables
#define model 100500
#define IR_Left_Pin A0 
#define IR_Right_Pin A1
#define IR_Normalization 5
#define IR_Lower_Bound 60
#define IR_Upper_Bound 200
#define LED_LEFT_BLINDSPOT 13 //Left BlindSpot LED
#define LED_RIGHT_BLINDSPOT 12 //Right BlindSpot LED

//--------------------------



//Sharp IR Sensor Init
SharpIR myLeftSensor = SharpIR(IR_Left_Pin, model);
SharpIR myRightSensor = SharpIR(IR_Right_Pin, model);

// ProtoThreading Structures
static struct pt pt_Left_Blind, pt_Right_Blind;

int distance_left_cm;
int distance_right_cm;

static int BlindSpot(struct pt *ptB, int IR_distance, int LED_PIN){
  static unsigned long lastTimeBlinkB=0;
  PT_BEGIN(ptB);
  
  while(1){
      lastTimeBlinkB=millis();
      PT_WAIT_UNTIL(ptB,millis()-lastTimeBlinkB>IR_distance/IR_Normalization);
      lastTimeBlinkB=millis();
      PT_WAIT_UNTIL(ptB,millis()-lastTimeBlinkB>IR_distance/IR_Normalization);
      digitalWrite(LED_PIN,HIGH);
      lastTimeBlinkB=millis();
      PT_WAIT_UNTIL(ptB,millis()-lastTimeBlinkB>IR_distance/IR_Normalization);
      digitalWrite(LED_PIN,LOW);
    
  }

  PT_END(ptB);
}



void setup() {
  // put your setup code here, to run once:
    Serial.begin(9600);
    //Standard baud rate
    //Init the defined pins
     pinMode(LED_LEFT_BLINDSPOT,OUTPUT);
     pinMode(LED_RIGHT_BLINDSPOT,OUTPUT);

     //init the protothreading stuctures
     PT_INIT(&pt_Left_Blind);
     PT_INIT(&pt_Right_Blind);
     

}


void loop() {
  // put your main code here, to run repeatedly:
  IR_Left_Sensor();
  IR_Right_Sensor();

}


void IR_Left_Sensor(){
  // Get a distance measurement and store it as distance_cm:
  distance_left_cm = myLeftSensor.distance();
  
  // Print the measured distance to the serial monitor:
  Serial.print("LEFT Mean distance: ");
  Serial.print(distance_left_cm);
  Serial.println("cm");
    if(distance_left_cm < IR_Upper_Bound && distance_left_cm > IR_Lower_Bound)
  {
    BlindSpot(&pt_Left_Blind,distance_left_cm,LED_LEFT_BLINDSPOT);
      
  }else
  {
    digitalWrite(LED_LEFT_BLINDSPOT,LOW);
    //Serial.println("Nothing in me blindspot");
  }
}

void IR_Right_Sensor(){
  // Get a distance measurement and store it as distance_cm:
  distance_right_cm = myRightSensor.distance();
  
  // Print the measured distance to the serial monitor:
  Serial.print("Right Mean distance: ");
  Serial.print(distance_right_cm);
  Serial.println("cm");
    if(distance_right_cm < IR_Upper_Bound && distance_right_cm > IR_Lower_Bound)
  {
    BlindSpot(&pt_Right_Blind,distance_right_cm,LED_RIGHT_BLINDSPOT);
      
  }else
  {
    digitalWrite(LED_RIGHT_BLINDSPOT,LOW);
    //Serial.println("Nothing in me blindspot");
  }
}
