//Pt library used for protothreading
#include <pt.h>




// Definitions for variables and pins
#define Turning_Frequency 1000
#define TURN_INTERUPTL 7
#define TURN_INTERUPTR 3// On our board Pin 3 is an interupt pin would need to check for other boards
#define Left_Turn_LED 8 
#define Right_Turn_LED 9
#define Left_Turn_LED 8
#define Right_Turn_LED 9

int val;
int val2;
int val3;
//------------------------------------
//Interrupt triggers
volatile byte leftTurnS = LOW;
volatile byte turn = LOW;
volatile byte rightTurnS = LOW;

//Decleration of protothreading variables and functions
//Pt pointers to be used
static struct pt pt_Left_Turn, pt_Right_Turn ;


//Left Turn function to turn on the the left turn light, at a 1Hz frequency
static int leftTurn(struct pt *pt){
  static unsigned long lastTimeBlinkL=0;
  PT_BEGIN(pt);
  while(1){
      lastTimeBlinkL=millis();
      //10 msec delay to allow other threads to be run
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlinkL>10);
   if(leftTurnS==HIGH && rightTurnS==LOW){
      lastTimeBlinkL=millis();
      Serial.println("Turning Left");
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlinkL>Turning_Frequency);
      digitalWrite(Left_Turn_LED,HIGH);
      lastTimeBlinkL=millis();
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlinkL>Turning_Frequency);
      digitalWrite(Left_Turn_LED,LOW);
    }
  }

  PT_END(pt);
}


//Left Turn function to turn on the the Right turn light, at a 1Hz frequency
static int rightTurn(struct pt *ptR){
  static unsigned long lastTimeBlinkR=0;
  PT_BEGIN(ptR);
  while(1){
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>10);
   if(rightTurnS==HIGH && leftTurnS == LOW){
    Serial.println("Turning Right");
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>Turning_Frequency);
      digitalWrite(Right_Turn_LED,HIGH);
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>Turning_Frequency);
      digitalWrite(Right_Turn_LED,LOW);
    }
  }

  PT_END(ptR);
}



//-----------------------------------------------------
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  //Init of protothreading functions
  PT_INIT(&pt_Left_Turn);
  PT_INIT(&pt_Right_Turn);

  //Init the interupts pins
  pinMode(TURN_INTERUPTR,INPUT);
  pinMode(TURN_INTERUPTL,INPUT);

  attachInterrupt(digitalPinToInterrupt(TURN_INTERUPTR), stateTurnR, RISING);
  attachInterrupt(digitalPinToInterrupt(TURN_INTERUPTL), stateTurnL, RISING);
  //-----------------------
}

void loop() {
  // put your main code here, to run repeatedly:


  leftTurn(&pt_Left_Turn);
  rightTurn(&pt_Right_Turn);
  
}




void stateTurnR(){
    rightTurnS=!rightTurnS;
    leftTurnS=LOW;
}
void stateTurnL(){
    leftTurnS=!leftTurnS;
    rightTurnS=LOW;
}
