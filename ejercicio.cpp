#include <iostream>
using namespace std;

int main() {
    int num;
    cout << "¿Cuantos numeros vas a ingresar?: ";
    cin >> num;

    double suma = 0, numero;

    for (int i = 1; i <= num; i++) {
        cout << "Numero " << i << ": ";
        cin >> numero;
        suma += numero;
    }

    double media = suma / num;

    cout << "La media de los " << num << " numeros es: " << media << endl;

    return 0;
}

