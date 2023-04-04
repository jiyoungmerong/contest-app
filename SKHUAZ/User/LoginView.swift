import SwiftUI

struct LoginView: View {
    
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var showAlert = false
    @State private var alertMessage = ""
    @State private var loginSuccess = false
    
    var body: some View {
        NavigationView {
            VStack {
                Image("SKHUAZ")
                    .resizable()
                    .frame(width: 250, height: 60)
                    .padding(.bottom)
                TextField("아이디를 입력해주세요", text: $email)
                    .padding()
                    .frame(width: 350, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
                SecureField("비밀번호를 입력해주세요", text: $password)
                    .padding()
                    .frame(width: 350, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
                Button(action: {
                    login()
                }, label: {
                    Text("로그인")
                        .frame(width: 330, height: 10)
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color(red: 0.603, green: 0.756, blue: 0.819))
                        .cornerRadius(10)
                })
                HStack{
                    Spacer()
                    NavigationLink(
                        destination: FindView(),
                        label:{
                            Text("아이디/비밀번호 찾기")
                                .font(.system(size: 10))
                                .foregroundColor(Color.gray)
                        })
                    NavigationLink(destination: SignUpView().navigationBarTitle(Text("SKHUAZ"), displayMode: .inline)) {
                        Text("회원가입")
                            .font(.system(size: 10))
                            .foregroundColor(Color.gray)
                            .padding(.trailing)
                    }
                }
            }
            
        }.padding()
            .alert(isPresented: $showAlert, content: {
                Alert(title: Text("알림"), message: Text(alertMessage), dismissButton: .default(Text("확인")))
            })
            .fullScreenCover(isPresented: $loginSuccess, content: {
                TabbarView()
            })
    }
    
    
    func login() {
        guard let url = URL(string: "http://skhuaz.duckdns.org/users/new-user") else {
            self.alertMessage = "Invalid URL"
            self.showAlert = true
            return
        }
        let body: [String: Any] = [ "email": self.email, "password": self.password]
        let bodyData = try? JSONSerialization.data(withJSONObject: body)
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.httpBody = bodyData
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                self.alertMessage = error.localizedDescription
                self.showAlert = true
                return
            }
            
            guard let httpResponse = response as? HTTPURLResponse,
                  (200..<300).contains(httpResponse.statusCode) else {
                self.alertMessage = "로그인 실패"
                self.showAlert = true
                return
            }
            
            guard let data = data,
                  let json = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] else {
                self.alertMessage = "Invalid data"
                self.showAlert = true
                return
            }
            
            guard let success = json["success"] as? Bool, success else {
                self.alertMessage = "로그인 실패"
                self.showAlert = true
                return
            }
            
            // 로그인에 성공한 경우
            self.loginSuccess = true
            print(json)
        }.resume()
    }

}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}



