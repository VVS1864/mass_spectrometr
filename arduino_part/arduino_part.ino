String spacer = ", ";
int mass = 150;
boolean send_flag = false;
int cmd;
char start_cmd = '1';
char stop_cmd = '0';

int led = 13;//
void setup() {
  pinMode(led, OUTPUT);
  digitalWrite(led, LOW);
  Serial.begin(9600); 
}

// the loop function runs over and over again forever
void loop() {
  recvOneChar();
  send_data();
  
}
void recvOneChar(){
 if(Serial.available()){
  cmd = Serial.read();
  
  if(cmd == start_cmd){
    send_flag = true;
    digitalWrite(led, HIGH);
  }
  else{
    send_flag = false;
    digitalWrite(led, LOW);
  }
 } 
}
void send_data(){
  if(send_flag == true){
    Serial.print("<");
    Serial.print(mass);
    Serial.print(spacer);
    Serial.print(random(100));
    Serial.print(spacer);
    Serial.print(random(100));
    Serial.print(spacer);
    Serial.print(random(100));
    Serial.print(">");
    Serial.println ();
    mass--;
    delay(500);
  }
}





