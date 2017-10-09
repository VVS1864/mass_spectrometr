String spacer = ", ";
double mass = 150.0;
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
    int r = 100;
    int n = 0;
    int x = random(40);
    if (x == 3) {
      r = 1000;
      n = 500;
    }
    Serial.print("<");
    Serial.print(mass);
    Serial.print(spacer);
    Serial.print(random(n, r));
    Serial.print(spacer);
    Serial.print(random(100));
    Serial.print(spacer);
    Serial.print(random(100));
    Serial.print(">");

    mass=mass-0.1;
    delay(50);
  }
}






