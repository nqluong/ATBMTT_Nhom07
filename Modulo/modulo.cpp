#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <numeric>
#include <fstream>
#include <sstream>
using namespace std;

// Hàm tính luy thua modulo bang phuong pháp bình phuong có l?p
long long powerMod(long long a, long long m, long long n) {
    long long result = 1;
    a = a % n;
    while (m > 0) {
        if (m % 2 == 1)
            result = (result * a) % n;
        m = m / 2;
        a = (a * a) % n;
    }
    return result;
}

// nghich dao modulo theo thuat toán Euclid mo rong
long long gcdExtended(long long a, long long b, long long *x, long long *y) {
    if (a == 0) {
        *x = 0, *y = 1;
        return b;
    }
    long long x1, y1;
    long long gcd = gcdExtended(b % a, a, &x1, &y1);
    *x = y1 - (b / a) * x1;
    *y = x1;
    return gcd;
}
long long gcd(long long a, long long b) {
    if (b == 0) return a;
    return gcd(b, a % b);
}

long long modInverse(long long a, long long n) {
    long long x, y;
    long long g = gcdExtended(a, n, &x, &y);
    if (g != 1) return -1;
    return (x % n + n) % n;
}
long long eulerPhi(long long n) {
    long long result = n;
    for (long long p = 2; p * p <= n; p++) {
        if (n % p == 0) { 
            while (n % p == 0) 
                n /= p; 
            result -= result / p;
        }
    }
    if (n > 1) 
        result -= result / n;
    return result;
}
long long eulerModPower(long long a, long long m, long long n) {
    if (gcd(a,n) != 1) {
        cout << "Khong the ap dung dinh ly Euler vi a va n khong nguyen to cung nhau!\n";
        return -1;
    }

    long long phi_n = eulerPhi(n); 
    long long reduced_m = m % phi_n; 

    return powerMod(a, reduced_m, n); 
}

// Hàm kiem tra so nguyên to
bool isPrime(long long n) {
    if (n < 2) return false;
    for (long long i = 2; i * i <= n; i++) {
        if (n % i == 0) return false;
    }
    return true;
}
long long fermatModPower(long long a, long long m, long long n) {

    if (isPrime(n) && a % n != 0) {
        m = m % (n - 1);  
    }
    
    
    long long result = 1;
    a = a % n;  
    
    while (m > 0) {
        if (m % 2 == 1)
            result = (result * a) % n;
        m = m / 2;
        a = (a * a) % n;
    }
    
    return result;
}


// Hàm gi?i h? phuong trình d?ng du b?ng d?nh lý s? du Trung Hoa
long long chineseRemainderTheorem(vector<long long> num, vector<long long> rem) {
    long long M = 1; 
    for (long long n : rem) M *= n;

    long long result = 0;
    for (size_t i = 0; i < num.size(); i++) {
        long long Mi = M / rem[i]; 
        long long inv = modInverse(Mi, rem[i]); 
        result += num[i] * Mi * inv; 
    }
    return (result % M ); 
}

// Hàm ki?m tra xem a có ph?i can nguyên th?y c?a n không
bool isPrimitiveRoot(long long a, long long n) {
    if (gcd(a, n) != 1) return false;
    long long phi = n - 1;
    set<long long> factors;
    long long temp = phi;
    for (long long i = 2; i * i <= temp; i++) {
        while (temp % i == 0) {
            factors.insert(i);
            temp /= i;
        }
    }
    if (temp > 1) factors.insert(temp);
    
    for (long long factor : factors) {
        if (powerMod(a, phi / factor, n) == 1)
            return false;
    }
    return true;
}

long long LOGARITHM(long long a,long long b, long long n) {
	long long value = 1;
	for(long long i=1 ;i<n;i++) {
		value=(value * a) % n;
		if(b == value) return i;
	}
}
void GTBT(long long a,long long b, long x, long long y, long long n) {
	long long A1,A2,A3,A4,A5;
	long long ax=1,by=1;			
	A4=modInverse(powerMod(b,y,n),n);
	cout<< "\nA1 = (a^x + b^y) mod n = "<<(powerMod(a,x,n)+powerMod(b,y,n))%n;
	cout << "\nA2 = (a^x - b^y) mod n = "<<(powerMod(a,x,n)-powerMod(b,y,n))%n;
	cout << "\nA3 = (a^x * b^y) mod n = "<<(powerMod(a,x,n)*powerMod(b,y,n))%n;
	cout << "\nA4 = (b^y)^-1 mod n = "<<modInverse(powerMod(b,y,n),n);
	cout << "\nA5 = (a^x /b^y) mod n = "<<(powerMod(a,x,n)*A4)%n;
}

vector<long long> factorize(long long n) {
    vector<long long> factors;
    for (long long i = 2; i * i <= n; i++) {
        while (n % i == 0) {
            factors.push_back(i);
            n /= i;
        }
    }
    if (n > 1) factors.push_back(n);
    return factors;
}


long long crtModPower(long long a, long long k, long long n) {

    if (n == 1) return 0;
    if (a == 0) return 0;
    if (k == 0) return 1;
    if (k == 1) return a % n;

    vector<long long> factors = factorize(n);

    set<long long> uniqueFactors(factors.begin(), factors.end());
    vector<long long> primes(uniqueFactors.begin(), uniqueFactors.end());

    vector<long long> primePowers;
    for (long long p : primes) {
        long long power = 1;
        while (n % (power * p) == 0) {
            power *= p;
        }
        primePowers.push_back(power);
    }

    vector<long long> remainders;
    for (size_t i = 0; i < primes.size(); i++) {
        long long p = primes[i];
        long long power = primePowers[i];
        
    
        remainders.push_back(powerMod(a, k, power));
    }
    
    return chineseRemainderTheorem(remainders, primePowers);
}
int main() {
    int choice;
    ifstream file("inputmodulo.txt");
    if (!file.is_open()) {
        cout << "Khong the mo file input.txt!" << endl;
        return 1;
    }
    

    vector<string> lines;
    string line;
    while (getline(file, line)) {
        lines.push_back(line);
    }
    file.close();
    

    
    do {
        cout << "\n----------------MENU----------------";
        cout << "\n1. Tinh luy thua modulo";
        cout << "\n2. Tinh nghich dao modulo";
        cout << "\n3. Dinh ly FERMAT de tinh luy Thua MODULO";
        cout << "\n4. TiNH GIa TRi HaM EULER";
        cout << "\n5. Su dung dinh ly EULER de tinh luy thua MODULO";
        cout << "\n6. Su dung dinh ly so du Trung Hoa de tinh luy thua";
        cout << "\n7. Giai he phuong trinh dang du (Dinh ly so du Trung Hoa)";
        cout << "\n8. Kiem tra can nguyen thuy";
        cout << "\n9. Tim LOGARITHM roi rac";
        cout << "\n10. Tinh cac bieu thuc modulo co ban";
        cout << "\n11. Thoat chuong trinh";        
        cout << "\nChon chuc nang: ";
        cin >> choice;
        if (choice < 1 || choice > 11) {
            cout << "Lua chon khong hop le!" << endl;
            continue;
        }
		 if (choice == 10) {
            cout << "Thoat chuong trinh..." << endl;
            break;
        }
        if (choice <= lines.size()) {
        	line = lines[choice-1];  
            istringstream iss(line);
            
            if (choice == 1) {
                long long a, m, n;
                if (iss >> a >> m >> n) {
                    cout << "a = " << a << ", m = " << m << ", n = " << n << endl;
                    cout << "Ket qua: "<<" a^m mod n = " << powerMod(a, m, n) << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 1" << endl;
                }
            } else if (choice == 2) {
                long long a, n;
                if (iss >> a >> n) {
                    cout << "a = " << a << ", n = " << n << endl;
                    long long result = modInverse(a, n);
                    if (result == -1) cout << "Khong ton tai nghich dao modulo" << endl;
                    else cout << "Nghich dao modulo: a^(-1) mod n = " << result << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 2" << endl;
                }
            } else if (choice == 3) {
                long long a, m, n;
                if (iss >> a >> m >> n) {
                    cout << "a = " << a << ", m = " << m << ", n = " << n << endl;
                    cout << "b = a^m mod n = " << fermatModPower(a, m, n) << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 3" << endl;
                }
            } else if (choice == 4) {
                long long n;
                if (iss >> n) {
                    cout << "n = " << n << endl;
                    cout << "Phi(n) = " << eulerPhi(n) << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 4" << endl;
                }
            } else if (choice == 5) {
                long long a, m, n;
                if (iss >> a >> m >> n) {
                    cout << "a = " << a << ", m = " << m << ", n = " << n << endl;
                    long long result = eulerModPower(a, m, n);
                    if (result != -1) cout << "b = a^m mod n = " << result << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 5" << endl;
                }
            }
			else if (choice == 6) {
			    long long a, k, n;
			    if (iss >> a >> k >> n) {
			        cout << "a = " << a << " k = " << k << " n = " << n << endl;
			        cout << "b = a^k mod n (using CRT) = " << crtModPower(a, k, n) << endl;
			    } else {
			        cout << "Loi doc du lieu tu file cho lua chon 10" << endl;
			    }
			} 
			else if (choice == 7) {
                vector<long long> num, rem;
                long long val;
                int index = 0;
                while (iss >> val) {
                    if (index % 2 == 0)
                        rem.push_back(val);
                    else
                        num.push_back(val);
                    index++;
                }
                if (!num.empty() && rem.size() == num.size()) {
                    cout << "He phuong trinh:" << endl;
                    for (size_t i = 0; i < num.size(); i++) {
                        cout << "x mod " << rem[i] << " = " << num[i]  << endl;
                    }
                    cout << "Nghiem cua he: " << chineseRemainderTheorem(num, rem) << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 6" << endl;
                }
            } else if (choice == 8) {
                long long a, n;
                if (iss >> a >> n) {
                    cout << "a = " << a << ", n = " << n << endl;
                    if (isPrimitiveRoot(a, n)) cout << a << " la can nguyen thuy cua " << n << endl;
                    else cout << a << " khong phai can nguyen thuy cua " << n << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 7" << endl;
                }
            } else if (choice == 9) {
                long long a, b, n;
                if (iss >> a >> b >> n) {
                    cout << "a = " << a << ", b = " << b << ", n = " << n << endl;
                    cout << "k = " << LOGARITHM(a, b, n) << endl;
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 8" << endl;
                }
            } else if (choice == 10) {
                long long a, b, x, y, n;
                if (iss >> a >> b >> x >> y >> n) {
                    cout << "a = " << a << ", b = " << b << ", x = " << x << ", y = " << y << ", n = " << n << endl;
                    GTBT(a, b, x, y, n);
                } else {
                    cout << "Loi doc du lieu tu file cho lua chon 9" << endl;
                }
            }
        } else {
            cout << "Het du lieu trong file input!" << endl;
        }
        
    } while (true);
    
    cout << "Thoat chuong trinh..." << endl;
    return 0;
}


