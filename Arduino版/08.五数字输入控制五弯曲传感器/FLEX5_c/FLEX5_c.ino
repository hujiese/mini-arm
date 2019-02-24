#define DLPINS 1
#define LEDS 7

//int dlPotPins[DLPINS] = {0, 1, 2, 3, 4};
int dlPotPins[DLPINS] = {0};
int dlValues[DLPINS] = {0};
int dlResults[DLPINS] = {0};
int ledPins[LEDS] = {2, 3, 4, 5, 6, 7, 8};

int val = 0;
int result = 0;
void setPowerLight(int val);

void setup() {
  int i;
  for(i = 0;i < LEDS;i++)
  {
     pinMode(ledPins[i], OUTPUT);
  }
  setPowerLight(0);
  Serial.begin(9600);
}

void loop() {
  int i, j;
  for(i = 0;i < DLPINS;i++)
  {
     dlValues[i] = analogRead(dlPotPins[i]);
     Serial.println(dlValues[i]);
  } 
  Serial.println("---------------------------------------");
  for(j = 0;j < DLPINS;j++)
  {
      dlResults[j] = 0 * (dlValues[j] <= 560) + 1 * (dlValues[j] > 560 && dlValues[j] <= 595) + 2 * (dlValues[j] > 595 && dlValues[j] <= 630) + 
                     3 * (dlValues[j] > 630 && dlValues[j] <= 665) + 4 * (dlValues[j] > 665 && dlValues[j] <= 700) + 5 * (dlValues[j] > 700 && dlValues[j] <= 735) +
                     6 * (dlValues[j] > 735 && dlValues[j] <= 770) + 7 *(dlValues[j] > 770 && dlValues[j] <= 805); 
      setPowerLight(dlResults[j]); 
      delay(1000);
  }
}
void setPowerLight(int val)
{
  int i, j;
  for(i = 0;i < val;i++)
  {
      digitalWrite(ledPins[i], LOW);
  }
  for(j = val;j < LEDS;j++)
  {
      digitalWrite(ledPins[j], HIGH);
  }
}

