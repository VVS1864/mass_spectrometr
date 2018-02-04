unsigned long current_time;
int mass = 1224;
int init_mass = 1224;
int en_el;
int intensity;
//bytes for transfer [current_time + mass + en_el + intensity]
byte buf[4+2+2+2];
boolean back_flag = false;
//int v[2];
int led = 13;
void setup() {
  pinMode(led, OUTPUT);
  digitalWrite(led, LOW);
  Serial.begin(38400); 
}

// the loop function runs over and over again forever
void loop() {
  set_mass();
  set_data();
  send_data();
  delay(100);
}
void set_mass(){
  set_demo_mass();
}
void set_demo_mass(){
  if (back_flag == false){
    if(mass>0){
      mass-=4;
    }
    else{
      mass = 0;
      back_flag = true;
    }
  }
  else{
    if(mass<init_mass){
      mass+=4;
    }
  
    else{
      mass = init_mass;
      back_flag = false;
    }
  }
}

void set_data(){
  set_random();
}

void set_random(){
  current_time = millis();
  int r = 10;
  int n = 0;
  int x = random(50);
  if (x == 3) {
    r = 1000;
    n = 500;
  }
  en_el = random(30);
  intensity = random(n, r);
}

void send_data(){
  ints_to_bytes();
  Serial.write(buf, 10); 
}

/*
void bytes_to_int(byte bytes[4], int val[2]){
 val[0] = ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
 val[1] = ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
 }*/
void ints_to_bytes(){
  //unsigned long time
  buf[0] = (current_time >> 24) & 0xFF;
  buf[1] = (current_time >> 16) & 0xFF;
  buf[2] = (current_time >> 8) & 0xFF;
  buf[3] = current_time & 0xFF;

  //int mass
  buf[4] = (mass >> 8) & 0xFF;
  buf[5] = mass & 0xFF;

  //int en_el
  buf[6] = (en_el >> 8) & 0xFF;
  buf[7] = en_el & 0xFF;

  //int intensity
  buf[8] = (intensity >> 8) & 0xFF;
  buf[9] = intensity & 0xFF;
}












