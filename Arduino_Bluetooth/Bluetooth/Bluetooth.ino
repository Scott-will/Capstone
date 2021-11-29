
class BluetoothHandler{
  //public methods and properties
  public:
  bool connected;

    void Listen(){
    while(Serial.available()){
      message = Serial.read();
      Serial.println(message);
      if(message == 1){
        digitalWrite(8, HIGH);
      }
    }
  }

  //private methods and properties
  private:
  String message; 
};

BluetoothHandler handler;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(8, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
    handler.Listen();
    
}
