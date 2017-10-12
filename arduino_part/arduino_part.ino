String spacer = ", ";
int mass = 1500;
boolean send_flag = false;
int cmd;
char start_cmd = '1';
char stop_cmd = '0';
byte buf[4];
int v[2];
int led = 13;//
void setup() {
  pinMode(led, OUTPUT);
  digitalWrite(led, LOW);
  Serial.begin(115200); 
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
  if(send_flag == true && mass>0){
    int r = 10;
    int n = 0;
    int x = random(60);
    if (x == 3) {
      r = 100;
      n = 50;
    }
   
    int data2 = random(n, r);
    
    ints_to_bytes(mass, data2, buf);
    Serial.write(buf, 4);
    mass--;
  }
}
void bytes_to_int(byte bytes[4], int val[2]){
  val[0] = ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
  val[1] = ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
}
void ints_to_bytes(int n1, int n2, byte bytes[4]){
  bytes[0] = (n1 >> 8) & 0xFF;
  bytes[1] = n1 & 0xFF;
  
  bytes[2] = (n2 >> 8) & 0xFF;
  bytes[3] = n2 & 0xFF;
  /*
  bytes[0] = (byte) n1 >> 8;
  bytes[1] = (byte) n1;
  
  bytes[2] = (byte) n2 >> 8;
  bytes[3] = (byte) n2;
  */
  /*
  bytes[0] = (byte) n1 >> 24;
  bytes[1] = (byte) n1 >> 16;
  bytes[2] = (byte) n1 >> 8;
  bytes[3] = (byte) n1;
  
  bytes[4] = (byte) n2 >> 24;
  bytes[5] = (byte) n2 >> 16;
  bytes[6] = (byte) n2 >> 8;
  bytes[7] = (byte) n2;
  */
  /*
  bytes[0] = (n2 >> 24) & 0xFF;
  bytes[1] = (n2 >> 16) & 0xFF;
  bytes[2] = (n2 >> 8) & 0xFF;
  bytes[3] = n2 & 0xFF;
  
  bytes[4] = (n1 >> 24) & 0xFF;
  bytes[5] = (n1 >> 16) & 0xFF;
  bytes[6] = (n1 >> 8) & 0xFF;
  bytes[7] = n1 & 0xFF;
  */
}







