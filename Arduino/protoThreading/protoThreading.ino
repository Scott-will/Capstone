/*ProtoThreading file
 * Primary file for teh arduino program to control all periphals
 * Written by group
 * 
 */

 //Pt library used for protothreading
#include <pt.h>

//Definations for pins
#define TURN_INTERUPT 3
#define LED_1_PIN 9
#define LED_2_PIN 4
#define BUTTON_PIN 5
#define BUTTON_PIN2 7
#define anag A1

//Pt pointers to be used
static struct pt pt1 , pt2,pt3 ;
//Interrupt triggers
volatile byte leftTurnS = LOW;
volatile byte rightTurnS = LOW;

//Class is commented out for now, having difficulty with the message error without it being static - Will try again later - Kris

/*class BluetoothHandler{
  //public methods and properties
  public:
  bool connected;
  static String message; 
  static char charac;

  static int list(struct pt *ptL){
  static unsigned long lastL=0;
  PT_BEGIN(ptL);
  while(1){
      lastL=millis();
      PT_WAIT_UNTIL(ptL,millis()-lastL>10);
  //  Serial.println("lol fuck youR");
   if(Serial.available()){
    Serial.println("Listening");
      lastL=millis();
      charac=Serial.read();
      message.concat(charac);
      Serial.println(charac);
      if(charac='#'){
        Serial.println(message);
        digitalWrite(8,HIGH);
        message="";
      }
    }
  }
  PT_END(ptL);
}



}; */

/* List Function
 * Listens to bluetooth connection and will concate characters into messages
 * '#' is temporarily used for breaklines
 * Scot feel free to mess with this function
 */
static int list(struct pt *ptL){
  String message;
  char charac;
  static unsigned long lastL=0;
  PT_BEGIN(ptL);
  while(1){
      lastL=millis();
      PT_WAIT_UNTIL(ptL,millis()-lastL>10);
  //  Serial.println("lol fuck youR");
   if(Serial.available()){
    Serial.println("Listening");
      lastL=millis();
      charac=Serial.read();
      message.concat(charac);
      Serial.println(charac);
      if(charac='#'){
        Serial.println(message);
        digitalWrite(8,HIGH);
        message="";
      }
    }
  }
  PT_END(ptL);
}// */

//Left Turn function to turn on the the left turn light, at a 1Hz frequency
static int leftTurn(struct pt *pt){
  static unsigned long lastTimeBlink=0;
  PT_BEGIN(pt);
  while(1){
      lastTimeBlink=millis();
      //10 msec delay to allow other threads to be run
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlink>10);
  //  Serial.println("lol fuck you1");
   if(leftTurnS==HIGH && rightTurnS==LOW){
      lastTimeBlink=millis();
      Serial.println("Turning Left");
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlink>1000);
      digitalWrite(LED_1_PIN,HIGH);
      lastTimeBlink=millis();
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlink>1000);
      digitalWrite(LED_1_PIN,LOW);
    }
  }

  PT_END(pt);
}


static int rightTurn(struct pt *ptR){
  static unsigned long lastTimeBlinkR=0;
  PT_BEGIN(ptR);
  while(1){
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>10);
  //  Serial.println("lol fuck youR");
   if(rightTurnS==HIGH && leftTurnS == LOW){
    Serial.println("Turning Right");
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>1000);
      digitalWrite(LED_2_PIN,HIGH);
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>1000);
      digitalWrite(LED_2_PIN,LOW);
    }
  }

  PT_END(ptR);
}

static int count = 0;
static int Rcount = 0;
//BluetoothHandler handle;
void setup(){
 // put your setup code here, to run once:
 pinMode(LED_1_PIN,OUTPUT);
 pinMode(LED_2_PIN,OUTPUT);
 PT_INIT(&pt1);
 PT_INIT(&pt2);
 pinMode(BUTTON_PIN, INPUT);
 pinMode(8, OUTPUT); //Bluetooth
 pinMode(BUTTON_PIN2, INPUT);
 pinMode(TURN_INTERUPT,INPUT);
 attachInterrupt(digitalPinToInterrupt(TURN_INTERUPT), stateTurn, RISING);
 //attachInterrupt(digitalPinToInterrupt(BUTTON_PIN2), stateRightTurn, RISING);
}


void loop() {
  // put your main code here, to run repeatedly:
  leftTurn(&pt1);
  rightTurn(&pt2);
  //handle.list(&pt3);
  list(&pt3);
  
}


void stateTurn()
{
  int val = analogRead(A1);
  Serial.println(val);
  if(val>590){
    leftTurnS=!leftTurnS;
    rightTurnS=LOW;
  }else{
    rightTurnS=!rightTurnS;
    leftTurnS=LOW;
  }
}




void stateLeftTurn() {
  leftTurnS = !leftTurnS;
  Serial.println("I am in state change");
  count++;
  Serial.println(count);
}


void stateRightTurn() {
  rightTurnS = !rightTurnS;
  Serial.println("I am in right state change");
  Rcount++;
  Serial.println(Rcount);
}
