/*
#include <Wire.h>
#include <Adafruit_MCP4725.h>
#include <Adafruit_ADS1015.h>
Adafruit_MCP4725 MCP4725;
Adafruit_ADS1115 ads(0x48);
*/
//#include <LiquidCrystal_PCF8574.h>
//LiquidCrystal_PCF8574 lcd(0x3F);

unsigned long current_time = 0;
int mass = 5000;
int init_mass = 5000;
int en_el;
int intensity;
char data_transfer_state = 'n';
char r_transfer_state;

int dac_voltage;

//bytes for transfer [current_time + mass + en_el + intensity]
byte buf[4+2+2+2+1];
boolean back_flag = false;
boolean send_flag = false;
char start_cmd = '1';

boolean fluctuation = false;
int fluctuation_rate = 0;

int led = 13;

char read_buf[3];

void setup() {
  pinMode(led, OUTPUT);
  Serial.begin(38400); 
  digitalWrite(led, LOW);
  /*
  MCP4725.begin(0x62);
  ads.begin();
  ads.setGain(GAIN_TWO);
  pinMode(A0, INPUT);
  */
  //lcd.begin(16, 2); 
  //lcd.setBacklight(255);
  //lcd.home(); lcd.clear();
  //lcd.print("Init");
}

// the loop function runs over and over again forever
void loop() {
  if(Serial.available() > 0){
    set_mass();
    set_en_el();
    set_data();
    send_data();
    //  output_lcd();
  }
}

void set_mass(){
  set_demo_mass();
  //mass = 32767 + ads.readADC_Differential_0_1();
  
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
    if (mass<0){
      mass = 0;
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
    if (mass > init_mass){
      mass = init_mass;
    }
  }
  if (mass > 4000 && mass < 4020 && fluctuation == false){
    fluctuation = true;
    fluctuation_rate = 10;
  }
  if (fluctuation){
    if (fluctuation_rate == 0){
      fluctuation = false;
      mass = 3900;
      return;
    }
    mass = random(1000, 1200);
    if (mass > 2000 && (fluctuation_rate == 5 || fluctuation_rate == 7 || fluctuation_rate == 3)){
      mass = random(1000, 4000);
    }
    fluctuation_rate--;
    
  }
}

void set_en_el(){
  en_el = dac_voltage;
  //MCP4725.setVoltage(en_el, false);
}

void set_data(){
  set_random();
  //intensity=analogRead(A0);
  current_time = millis();
}

void set_random(){
  int r = 10;
  int n = 2;
  int x = random(50);
  //if (x == 3) {
  
  if (mass > 500 && mass < 520){
    r = 1000;
    n = 500;
  }
  if (mass > 2000 && mass < 2020){
    r = 1000;
    n = 500;
  }
  if (mass > 4000 && mass < 4020){
    r = 1000;
    n = 500;
  }
  
  /*
  if (en_el > 100 && en_el < 200){
    intensity = 15;
  }
  */
  /*
  if(mass > 900 && mass < 1000){
    intensity = 100;
  }
  */
  //else{
    intensity = random(n, r);
  //}
  
}

void send_data(){

  if(send_flag == false){
    char cmd = Serial.read();
    if(cmd == start_cmd){
      send_flag = true;
      data_transfer_state = 't';
    }
  }

  if(send_flag == true){
    Serial.readBytes(read_buf, 3);
    bytes_to_ints();


    ints_to_bytes();
    Serial.write(buf, 11);


  }
}

/**
 * void output_lcd() {
 * 
 * lcd.home(); //lcd.clear();
 * lcd.print("mass: "); lcd.print(mass); lcd.print(";");
 * lcd.setCursor(0, 1); 
 * lcd.print("EE:"); lcd.print(en_el);lcd.print(";");
 * lcd.setCursor(8, 1);
 * lcd.print("INT: "); lcd.print(intensity);
 * 
 * }
 **/

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

  //char transfer status
  buf[10] = data_transfer_state;
}

void bytes_to_ints(){
  dac_voltage = ((read_buf[0] & 0xff) << 8) | (read_buf[1] & 0xff);
  r_transfer_state = char(read_buf[2]);
}




