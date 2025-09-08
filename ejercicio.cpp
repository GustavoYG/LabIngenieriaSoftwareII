#include <iostream>
using namespace std;

int main() {
    int n;
    cout << "¿Cuantos numeros vas a ingresar?: ";
    cin >> n;

    double suma = 0, numero;

    for (int i = 1; i <= n; i++) {
        cout << "Numero " << i << ": ";
        cin >> numero;
        suma += numero;
    }

    double media = suma / n;

    cout << "La media de los " << n << " numeros es: " << media << endl;

    return 0;
}

