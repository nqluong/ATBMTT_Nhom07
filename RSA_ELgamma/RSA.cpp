#include <iostream>
#include <cmath>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
using namespace std;

// Hàm tính luy th?a modulo
long long modPow(long long base, long long exp, long long mod) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) result = (result * base) % mod;
        base = (base * base) % mod;
        exp /= 2;
    }
    return result;
}

// Hàm d?c s? t? chu?i
vector<long long> readValues(const string& line) {
    vector<long long> values;
    stringstream ss(line);
    long long val;
    while (ss >> val) {
        values.push_back(val);
    }
    return values;
}

void diffieHellman(const string& line) {
    vector<long long> values = readValues(line);
    if (values.size() < 4) {
        cout << "Khong du gia tri cho Diffie-Hellman\n";
        return;
    }
    
    long long q = values[0];
    long long a = values[1];
    long long xA = values[2];
    long long xB = values[3];
    
    long long yA = modPow(a, xA, q);
    long long yB = modPow(a, xB, q);
    long long K_A = modPow(yB, xA, q);
    long long K_B = modPow(yA, xB, q);
    cout << "q = " << q << ", a = " << a << ", xA= " << xA << ", xB= " << xB << endl;
    cout << "yA = a^xA mod q = "<<a<<"^"<<xA<< " mod "<<q<<" = " << yA <<endl;
    cout << "yB = a^xB mod q = "<<a<<"^"<<xB<< " mod "<<q<<" = " << yB <<endl;
    cout << "K_A = yB^xA mod q = "<<yB<<"^"<<xA<<" mod "<<q<<" = "<<K_A<<endl;
    cout << "K_B = yA^xB mod q = "<<yA<<"^"<<xB<<" mod "<<q<<" = "<<K_B<<endl;
}

void rsa1(const string& line) {
    vector<long long> values = readValues(line);
    if (values.size() < 4) {
        cout << "Khong du gia tri cho RSA\n";
        return;
    }
    
    long long p = values[0];
    long long q = values[1];
    long long e = values[2];
    long long M = values[3];
    
    long long n = p * q;
    long long phi = (p - 1) * (q - 1);
    long long d = 0;
    
    for (long long i = 1; i < phi; i++) {
        if ((e * i) % phi == 1) {
            d = i;
            break;
        }
    }
    cout << "p = " << p << ", q = " << q << ", e = " << e << endl;
    cout << "phi_n = (p-1) * (q-1) = " <<p-1<<" * "<<q-1<<" = "<<phi<<"\n";
    cout <<"n = p * q = "<<p<<"*"<<q<<" = "<<n<<"\n";
    cout << "d = e^-1 mod phi_n = "<< e<<"^"<<"-1"<<" mod "<<phi<<" = "<<d<<"\n";
    cout << "PU = {e,n} = {" << e << ", " << n << "}\n";
    cout << "PR = {d,n} = {" << d << ", " << n << "}\n";
    
    long long C = modPow(M, d, n);
    cout << "Cach An tao ban ma hoa thong diep voi M = "<<M<<":\n";
    cout << "C = M^d mod n = "<<M<<"^"<<d<<" mod "<<n;
    cout << " => C = " << C << endl;
    
    long long M_dec = modPow(C, e, n);
    cout << "Cach nguoi nhan giai ban ma C: \n";
    cout << "M = C^e mod n = "<<C<<"^"<<e<<" mod "<<n;
    cout << " => M = " << M_dec << endl;  
	cout << "Ma hoa thuc hien nhiem vu chu ky so.\n";
    
}
void rsa2(const string& line) {
    vector<long long> values = readValues(line);
    if (values.size() < 4) {
        cout << "Khong du gia tri cho RSA\n";
        return;
    }
    
    long long p = values[0];
    long long q = values[1];
    long long e = values[2];
    long long M = values[3];
    
    long long n = p * q;
    long long phi = (p - 1) * (q - 1);
    long long d = 0;
    
    for (long long i = 1; i < phi; i++) {
        if ((e * i) % phi == 1) {
            d = i;
            break;
        }
    }
    cout << "p = " << p << ", q = " << q << ", e = " << e << endl;
    cout << "phi_n = (p-1) * (q-1) = " <<p-1<<" * "<<q-1<<" = "<<phi<<"\n";
    cout <<"n = p * q = "<<p<<"*"<<q<<" = "<<n<<"\n";
    cout << "d = e^-1 mod phi_n = "<< e<<"^"<<"-1"<<" mod "<<phi<<" = "<<d<<"\n";
    cout << "PU = {e,n} = {" << e << ", " << n << "}\n";
    cout << "PR = {d,n} = {" << d << ", " << n << "}\n";
    
    long long C = modPow(M, e, n);
    cout << "Cach nguoi gui(Ba) ma hoa thong diep voi M = "<<M<<":\n";
    cout << "C = M^e mod n = "<<M<<"^"<<e<<" mod "<<n;
    cout << " => C = " << C << endl;
    
    long long M_dec = modPow(C, d, n);
    cout << "Cach An giai ban ma C: \n";
    cout << "M = C^d mod n = "<<C<<"^"<<d<<" mod "<<n;
    cout << " => M = " << M_dec << endl;
    cout << "Ma hoa thuc hien nhiem vu bao mat.\n";
}
void elGamal(const string& line) {
    vector<long long> values = readValues(line);
    if (values.size() < 5) {
        cout << "Khong du gia tri cho ElGamal\n";
        return;
    }
    
    long long q = values[0];
    long long a = values[1];
    long long xA = values[2];
    long long k = values[3];
    long long M = values[4];
    
    long long yA = modPow(a, xA, q);
    cout << "q = " << q << ", a = " << a << ", xA = " << xA << ", k = " << k << ", M = " << M << endl;
    cout << "yA = a^xA mod q = "<<a<<"^"<<xA<<" mod "<<q<<" = "<< yA << "\n";
    cout << "PU={q,a,yA} = {"<< q <<","<< a <<","<<yA<<"}\n";
    
    long long C1 = modPow(a, k, q);
    long long C2 = (M * modPow(yA, k, q)) % q;
    
    cout << "Ba chon so k = "<<k<<" de ma hoa ban tin M = "<<M<< " gui cho An. Ban ma la (C1, C2)\n";
    cout<<" C1 = a^k mod q = "<<a<<"^"<<k<<" mod q = "<<C1<<"\n";
    cout<<" C2 = (M * yA^k mod q) mod q = ("<<M<<" * "<<yA<<"^"<<k<<" mod q )"<<" mod q = "<<C2<<"\n";
    cout << "=> Ciphertext = (C1,C2) = (" << C1 << ", " << C2 << ")\n";
    
    long long s = modPow(C1, q - 1 - xA, q);
    long long M_dec = (C2 * s) % q;
    
    cout<< "Cach An giai ban ma (C1,C2): \n";
    cout << "K = (C1)^xA mod q => " << "K^-1 = (C1)^(q-1-xA) mod q (dinh ly fermat nho) = "<<C1<<"^"<<q-1-xA<<" mod "<<q<<" = "<< s <<"\n";
    cout << "M = C2 * K^-1 mod q = "<< C2 <<" * "<< s << " mod "<<q<<"\n";
    cout << " => Decrypted M = " << M_dec << endl;
}

void dsa(const string& line) {
    vector<long long> values = readValues(line);
    if (values.size() < 5) {
        cout << "Khong du gia tri cho DSA\n";
        return;
    }
    
    long long p = values[0];
    long long q = values[1];
    long long h = values[2];
    long long xA = values[3];
    long long k = values[4];
    long long H = 19; 
    cout << "p = " << p << ", q = " << q << ", h = " << h << ", xA = " << xA << ", k = " << k << ", H = " << H << endl;
    long long g = modPow(h, (p - 1) / q, p);
    long long yA = modPow(g, xA, p);
    long long r = modPow(g, k, p) % q;
    long long s = (H + xA * r) * modPow(k, q - 2, q) % q;
    cout << "g = h^((p-1)/q) mod p = "<<g<<endl;
    cout << "yA = g^xA mod p = "<<g<<"^"<<xA<<" mod "<<p<<" = " << yA << "\n";
    cout << "r = (g^k mod p) mod q = "<<r<<endl;
    cout << "s =  ((H + xA * r) * k^(q-2) mod q) mod q = "<<s<<endl;
    cout << "Signature = (" << r << ", " << s << ")\n";
    
    long long w = modPow(s, q - 2, q);
    long long u1 = (H * w) % q;
    long long u2 = (r * w) % q;
    long long v = (modPow(g, u1, p) * modPow(yA, u2, p)) % p % q;
    
    cout << "Signature valid: " << (v == r ? "Yes" : "No") << endl;
}

int main() {
    ifstream inputFile("input.txt");
    
    if (!inputFile.is_open()) {
        cout << "Khong the mo file input.\n";
        return 1;
    }
    
    vector<string> inputLines;
    string line;
    
    while (getline(inputFile, line)) {
        inputLines.push_back(line);
    }
    
    inputFile.close();
    
 
    if (inputLines.size() < 4) {
        cout << "Not enough input lines in the file. Expected at least 4 lines.\n";
        return 1;
    }
    
    int choice;
    while (true) {
        cout << "----------------------\n";
        cout << "1. Diffie-Hellman\n";
        cout << "2. RSA1\n";
        cout << "3. RSA2\n";
        cout << "4. ElGamal\n";
        cout << "5. DSA\n";
        cout << "6. Thoat\n";
        cout << "Chon: ";
        cin >> choice;
        
        if (choice == 1) {
            diffieHellman(inputLines[0]);
        } else if (choice == 2) {
            rsa1(inputLines[1]);
        }else if (choice == 3) {
            rsa2(inputLines[2]);
        } else if (choice == 4) {
            elGamal(inputLines[3]);
        } else if (choice == 5) {
            dsa(inputLines[4]);
        } else if (choice == 6) {
            break;
        } else {
            cout << "Lua chon khong hop le. Vui long chon lai.\n";
        }
    }
    
    return 0;
}
