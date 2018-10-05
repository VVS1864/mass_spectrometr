unsigned long current_time = 0;
int mass = 1224;
int init_mass = 1224;
int en_el;
int intensity;

int dac_voltage;
//int start_e_scan; //0 - stop, 1 - start
//int start_voltage;
//int stop_voltage;
//float step_voltage;

//bytes for transfer [current_time + mass + en_el + intensity]
byte buf[4+2+2+2];
boolean back_flag = false;
boolean send_flag = false;
char start_cmd = '1';

int led = 13;

char read_buf[2];

void setup() {
  pinMode(led, OUTPUT);
  Serial.begin(38400); 
  digitalWrite(led, LOW);
}

// the loop function runs over and over again forever
void loop() {
  set_mass();
  set_en_el();
  set_data();
  send_data();
}
void set_mass(){
  set_demo_mass();
}
void set_demo_mass(){
  if (back_flag == false){
    if(mass>0){
      mass-=1 + random(-3, 3);
    }
    else{
      mass = 0;
      back_flag = true;
    }
  }
  else{
    if(mass<init_mass){
      mass+=1 + random(-3, 3);
    }

    else{
      mass = init_mass;
      back_flag = false;
    }
  }
}
void set_en_el(){
  en_el = dac_voltage;
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

  intensity = random(n, r);
}

void send_data(){
  if(send_flag == false){
    if(Serial.available()){
      char cmd = Serial.read();
      if(cmd == start_cmd){
        send_flag = true;
      }
    }
  }
  if(send_flag == true){

    Serial.readBytes(read_buf, 2);
    bytes_to_ints();

    ints_to_bytes();
    Serial.write(buf, 10);

  }
}

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

void bytes_to_ints(){
  dac_voltage = ((read_buf[0] & 0xff) << 8) | (read_buf[1] & 0xff);
  //start_e_scan = ((read_buf[2] & 0xff) << 8) | (read_buf[3] & 0xff);
  //start_voltage = ((read_buf[4] & 0xff) << 8) | (read_buf[5] & 0xff);
  //stop_voltage = ((read_buf[6] & 0xff) << 8) | (read_buf[7] & 0xff);
  //step_voltage = (((read_buf[8] & 0xff) << 8) | (read_buf[9] & 0xff)) * 0.01;
  //scan_cycle = ((read_buf[10] & 0xff) << 8) | (read_buf[11] & 0xff);
  
  /*
  if (dac_voltage == 107){
    digitalWrite(led, HIGH);
  }
  if (dac_voltage == 214){
    digitalWrite(led, LOW);
  }
  */
  
  /*
  current_time = (
   (read_buf[0] & 0xff) << 24 | 
   (read_buf[1] & 0xff) << 16 | 
   (read_buf[2] & 0xff) << 8 | 
   (read_buf[3] & 0xff)
   );
   mass = ((read_buf[4] & 0xff) << 8) | (read_buf[5] & 0xff);
   en_el = ((read_buf[6] & 0xff) << 8) | (read_buf[7] & 0xff);
   intensity = ((read_buf[8] & 0xff) << 8) | (read_buf[9] & 0xff);
   */
}



