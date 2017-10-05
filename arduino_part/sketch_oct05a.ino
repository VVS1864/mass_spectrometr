String spacer = ", ";
int mass = 150;
void setup() {
  Serial.begin(9600); 
}

// the loop function runs over and over again forever
void loop() {
  
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




