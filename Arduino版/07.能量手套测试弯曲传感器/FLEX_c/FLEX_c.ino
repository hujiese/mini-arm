int potPin = 0;
int ledPins[7] = {2, 3, 4, 5, 6, 7, 8};
int val = 0;
int result = 0;
void setPowerLight(int val);

void setup() {
  int i;
  for(i = 0;i < 7;i++)
  {
     pinMode(ledPins[i], OUTPUT);
  }
  setPowerLight(0);
  Serial.begin(9600);
}

void loop() {
  val = analogRead(potPin);
  Serial.println(val);
  
  result = 0 * (val <= 560) + 1 * (val > 560 && val <= 595) + 2 * (val > 595 && val <= 630) + 3 * (val > 630 && val <= 665) +
    4 * (val > 665 && val <= 700) + 5 * (val > 700 && val <= 735) + 6 * (val > 735 && val <= 770) + 7 *(val > 770 && val <= 805); 
  setPowerLight(result);
  
  delay(1000);
}
void setPowerLight(int val)
{
  int i, j;
  for(i = 0;i < 7;i++)
  {
      digitalWrite(ledPins[i], LOW);
  }
  for(j = val;j < 7;j++)
  {
      digitalWrite(ledPins[j], HIGH);
  }
}
