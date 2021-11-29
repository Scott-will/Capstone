
#include <pt.h>



#define TURN_INTERUPT 3
#define LED_1_PIN 9
#define LED_2_PIN 4
#define BUTTON_PIN 5
#define BUTTON_PIN2 7
#define anag A1

static struct pt pt1 , pt2,pt3 ;
volatile byte leftTurnS = LOW;
volatile byte rightTurnS = LOW;
static int i=0;

class BluetoothHandler{
  //public methods and properties
  public:
  bool connected;

    void Listen(struct pt *ptB){
    PT_BEGIN(ptB);
    while(Serial.available()){
      lastTimeBlink=millis();
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlink>10);
      message = Serial.read();
      Serial.println(message);
      if(message == 1){
        digitalWrite(8, HIGH);
      }
    }
    PT_END(pt);
  }

  //private methods and properties
  private:
  String message; 
};


static int leftTurn(struct pt *pt){
  static unsigned long lastTimeBlink=0;
  PT_BEGIN(pt);
  while(1){
      lastTimeBlink=millis();
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
BluetoothHandler handler;
void setup(){
 // put your setup code here, to run once:
 pinMode(LED_1_PIN,OUTPUT);
 pinMode(LED_2_PIN,OUTPUT);
 PT_INIT(&pt1);
 PT_INIT(&pt2);
 pinMode(BUTTON_PIN, INPUT);
 pinMode(8, OUTPUT);
 pinMode(BUTTON_PIN2, INPUT);
 pinMode(TURN_INTERUPT,INPUT);
 attachInterrupt(digitalPinToInterrupt(TURN_INTERUPT), stateTurn, RISING);
 //attachInterrupt(digitalPinToInterrupt(BUTTON_PIN2), stateRightTurn, RISING);
}


void loop() {
  // put your main code here, to run repeatedly:
  leftTurn(&pt1);
  rightTurn(&pt2);
  handler.Listen(&pt3);
  
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