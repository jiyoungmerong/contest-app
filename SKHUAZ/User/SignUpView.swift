import SwiftUI

struct SignUpView: View {
    
    @State private var Name: String = "" // 이름
    @State private var Nickname: String = "" // 닉네임
    @State private var email: String = "" // 이메일
    @State private var Password: String = "" // 비밀번호
    @State private var RepeatedPassword: String = ""
    @State private var Semester: Int = 0 // 학기
    @State private var passwordError = ""
    @State private var Graduate: Bool = false // 졸업여부
    @State private var Department: Bool = false // 전공미선택
    @State private var Major_minor: Bool = false // 주/부전공
    @State private var Double_major: Bool = false // 복수전공
    @State private var Major1: String = "IT융합자율학부" // 전공 1
    @State private var Major2: String = "전공2" // 전공 2
    
    var body: some View {
        Group{
            VStack(){
                TextInputView(
                    Name: self.$Name,
                    Nickname: self.$Nickname,
                    email: self.$email,
                    Password: self.$Password,
                    RepeatedPassword: self.$RepeatedPassword,
                    passwordError: self.$passwordError,
                    Semester: self.$Semester,
                    Graduate: self.$Graduate,
                    Department: self.$Department,
                    Major_minor: self.$Major_minor,
                    Double_major: self.$Double_major,
                    Major1: self.$Major1,
                    Major2: self.$Major2
                )
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
struct SignUp_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}

struct TextInputView: View {
    func checkEmail(str: String) -> Bool {
        let emailRegex = "[A-Z0-9a-z._%+-]+@office.skhu.ac.kr"
        return  NSPredicate(format: "SELF MATCHES %@", emailRegex).evaluate(with: str)
    }
    
    @Binding var Name: String
    // MARK: - 닉네임
    @Binding var Nickname: String
    @State private var isDuplicate: Bool?
    @State var nicknameSuccess: Bool = false
    @State private var showDuplicateAlert: Bool = false
    
    // MARK: - 이메일
    @Binding var email: String
    @State var ShowModel: Bool = false
    @State var CheckMessage: String = "ex) abc123@office.skhu.ac.kr"
    @State var emailSuccess: Bool = false
    // MARK: - 비밀번호
    @Binding var Password: String
    @Binding var RepeatedPassword: String
    @Binding var passwordError: String
    // MARK: - 학기
    @Binding var Semester: Int
    @State var SemesterMessage: String = "재학중인 학기를 선택하시오"
    

    // MARK: -
    let SelectedMajor1 = [
        "소프트웨어공학",
        "정보통신공학",
        "컴퓨터공학",
        "인공지능"
    ]
    let SelectedMajor2 = [
        "소프트웨어공학",
        "정보통신공학",
        "컴퓨터공학",
        "인공지능"
    ]
    // MARK: - 졸업 유무, 전공
    @Binding var Graduate: Bool // 졸업유무
    @Binding var Department: Bool // 전공 미선택
    @Binding var Major_minor: Bool // 주전공 - 부전공
    @Binding var Double_major: Bool // 복수전공
    @Binding var Major1: String // 전공1
    @Binding var Major2: String // 전공2
    @State var MarjorError: String = "선택한 두 전공이 같습니다" // 전공 선택 안내 메세지
    
    // MARK: - ModalView Code
    
    struct ModalView: View {
        @State private var code: String = ""
        @Environment(\.presentationMode) var presentationMode
        @Binding var ShowModel: Bool
        @State var showAlert: Bool = false
        @Binding var emailSuccess: Bool
        
        var body: some View {
            Group{
                VStack{
                    HStack{
                        Text("인증번호를 입력해주세요")
                            .foregroundColor(.black)
                        Spacer()
                    }
                    .padding(.leading, 30)
                    VStack(spacing: 0){
                        TextField("인증번호 입력", text: $code)
                            .frame(width: 350, height: 50)
                            .textFieldStyle(.roundedBorder)
                        HStack{
                            
                            Spacer()
                        }
                        .padding(.leading, 30)
                        
                    }
                    HStack{
                        Button(action: {
                            presentationMode.wrappedValue.dismiss()
                            self.ShowModel = false
                        }) {
                            Text("취소").bold()
                        }
                        .foregroundColor(.gray)
                        .frame(width: 170, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                        
                        Button(action: {
                            verifyConfirmationCode(cs: code)
                        }) {
                            Text("확인")
                        }
                        .alert(isPresented: $showAlert) {
                            Alert(
                                title: Text("인증번호가 올바르지 않습니다."),
                                message: nil,
                                dismissButton: .default(Text("확인"))
                            )
                        }
                        .foregroundColor(.white)
                        .frame(width: 170, height: 50)
                        .background(Color(red: 0.603, green: 0.756, blue: 0.819))
                        .cornerRadius(10)
                    }
                }
            }
        } // 모달뷰 body가 끝나는 곳
        func verifyConfirmationCode(cs: String) {  // 이메일 인증번호 확인 api
            guard let url = URL(string: "http://skhuaz.duckdns.org/verify?code=\(cs)") else {
                return
            }
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            
            
            let task = URLSession.shared.dataTask(with: request) { data, response, error in
                guard let data = data, let response = response as? HTTPURLResponse, error == nil else {
                    print("서버 응답에 실패했습니다.")
                    return
                }
                guard response.statusCode == 200 else {
                    print("서버 응답이 올바르지 않습니다.")
                    print(response.statusCode)
                    return
                }
                
                DispatchQueue.main.async {
                    if let result = String(data: data, encoding: .utf8) {
                        
                        let t = result.split(separator: ":")
                        
                        if t[1] == "true}" {
                            emailSuccess = true
                        } else {
                            emailSuccess = false
                        }
                        
                        if emailSuccess {
                            self.ShowModel = false
                            presentationMode.wrappedValue.dismiss()
                            print(emailSuccess)
                        }
                        else {
                            showAlert = true
                            print(emailSuccess)
                            
                        }
                    }
                }
            }
            task.resume()
        }
    }
    // Modal View 가 끝나는 곳
    // MARK: - email send api
    func sendEmail(email: String) {
        guard let url = URL(string: "http://skhuaz.duckdns.org/emailconfirm") else {
            return
        }
        let parameters = ["email": email]
        guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {
            return
        }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = httpBody
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error: \(error.localizedDescription)")
                return
            }
            guard let httpResponse = response as? HTTPURLResponse else {
                print("Invalid response")
                return
            }
            if httpResponse.statusCode == 200 {
                print("Email sent successfully")
            } else {
                print("Failed to send email. Error code: \(httpResponse.statusCode)")
            }
        }.resume()
    }
    // MARK: - SignUpView body 시작
    var body: some View {
        ScrollView{
            Text("  ") // 상단 여백을 주기 위함
                .padding(.bottom, 16)
            VStack{
                TextField("이름을 입력해주세요* ", text: $Name)
                    .padding()
                    .frame(width: 350, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
            }.padding(.bottom, 25)
            VStack{
                HStack{
                    TextField("닉네임을 입력해주세요* ", text: $Nickname)
                        .padding()
                        .frame(width: 250, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                    Button{
                        // 5. API 요청
                        let urlString = "http://skhuaz.duckdns.org/checkDuplicate/\(Nickname)".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
                        guard let url = URL(string: urlString) else { return }
                        
                        URLSession.shared.dataTask(with: url) { data, response, error in
                            guard let data = data, error == nil else {
                                // 6. 에러 발생시 처리
                                print(error?.localizedDescription ?? "Unknown error")
                                return
                            }
                            // 7. API 응답 결과 파싱
                            let responseString = String(data: data, encoding: .utf8)
                            isDuplicate = responseString?.lowercased() == "true"
                            if isDuplicate == true {
                                // 8. 중복 닉네임일 경우 팝업 표시
                                showDuplicateAlert = true
                            }
                        }.resume()
                    } label: {
                        Text("중복확인")
                            .foregroundColor(Color(red: 0.76, green: 0.552, blue: 0.552))
                            .frame(width: 90, height:50)
                            .background(RoundedRectangle(cornerRadius: 10).strokeBorder(Color(red: 0.76, green: 0.552, blue: 0.552)))
                    }
                    .alert(isPresented: $showDuplicateAlert, content: {
                        // 10. 중복 닉네임 알림 팝업
                        Alert(title: Text("닉네임 중복"), message: Text("입력한 '\(Nickname)'은 이미 사용중입니다."), dismissButton: .default(Text("확인")))
                    })
                }
                HStack{
                    if nicknameSuccess {
                        Text("닉네임 중복 확인 완료")
                            .font(.system(size: 14))
                            .foregroundColor(Color(red: 0.603, green: 0.756, blue: 0.819))
                            .lineLimit(2)
                        Spacer()
                    }
                    else{
                        Text(" ")
                    }
                }
            }
            .padding(.bottom, 5)
            VStack{
                HStack{
                    TextField("학교이메일을 입력해주세요* ", text: $email)
                        .padding()
                        .frame(width: 250, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                        .keyboardType(.emailAddress)
                        .autocapitalization(.none)
                    Button{
                        if checkEmail(str: email){
                            CheckMessage = ""
                            self.ShowModel = true
                            sendEmail(email: email)
                        }
                        else{
                            CheckMessage = "학교 이메일을 입력해주세요"
                        }
                    } label: {
                        Text("인증번호 발송")
                            .foregroundColor(Color(red: 0.76, green: 0.552, blue: 0.552))
                            .frame(width: 90, height:50)
                            .background(RoundedRectangle(cornerRadius: 10).strokeBorder(Color(red: 0.76, green: 0.552, blue: 0.552)))
                            .sheet(isPresented: self.$ShowModel) {
                                ModalView(ShowModel: $ShowModel, emailSuccess: $emailSuccess)
                            }
                        
                    }
                }
                HStack{
                    Text("\(CheckMessage)")
                        .font(.system(size: 14))
                        .foregroundColor(.gray)
                        .lineLimit(2)
                    Spacer()
                }
            }
            .padding(.bottom, 16)// 학번 중복확인 HStack
            
            
            VStack(spacing: 15){ // 비밀번호 입력 받는 SecureField
                SecureField("비밀번호를 입력해주세요 *", text: $Password)
                    .padding()
                    .frame(width: 350, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
                    .onChange(of: Password) { V in
                        if (Password == RepeatedPassword) {
                            passwordError = ""
                        }
                        else {
                            passwordError = "비밀번호가 일치하지 않습니다."
                        }
                    }
                    .padding(.bottom, 30)// 학번 중복확인 HStack
                
                VStack(spacing: 5){ // 비밀번호 입력 확인 안내문구와 SecureField
                    SecureField("비밀번호를 한번 더 입력해주세요*", text: $RepeatedPassword)
                        .padding()
                        .frame(width: 350, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                        .onChange(of: RepeatedPassword) { V in
                            if (Password == RepeatedPassword) {
                                passwordError = ""
                            }
                            else {
                                passwordError = "비밀번호가 일치하지 않습니다."
                            }
                        }
                    HStack{ // 비밀번호 안내문구 출력
                        Text(passwordError)
                            .padding(.leading, 30)
                            .font(.system(size: 14))
                            .foregroundColor(.red)
                        Spacer() // 문구 왼쪽 정렬을 위함
                    }
                } // 비밀번호 입력 확인 Field
            } // 비밀번호 입력 받는 Field
            .padding(.bottom, 30)// 학번 중복확인 HStack
            
            HStack(spacing: 30){
                Text("학기 선택")
                    .font(.system(size: 14))
                Group{
                    Menu {
                        Section(header: Text("재학중인 학기를 선택하시오")) {
                            Button(action: {
                                Semester = 8
                                SemesterMessage = "4학년 2학기"
                            }) {
                                Label("4학년 2학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 7
                                SemesterMessage = "4학년 1학기"
                            }) {
                                Label("4학년 1학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 6
                                SemesterMessage = "3학년 2학기"
                            }) {
                                Label("3학년 2학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 5
                                SemesterMessage = "3학년 1학기"
                            }) {
                                Label("3학년 1학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 4
                                SemesterMessage = "2학년 2학기"
                            }) {
                                Label("2학년 2학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 3
                                SemesterMessage = "2학년 1학기"
                            }) {
                                Label("2학년 1학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 2
                                SemesterMessage = "1학년 2학기"
                            }) {
                                Label("1학년 2학기", systemImage: "")
                            }
                            Button(action: {
                                Semester = 1
                                SemesterMessage = "1학년 1학기"
                            }) {
                                Label("1학년 1학기", systemImage: "")
                            }
                        }
                    }
                label: {
                    Label("\(SemesterMessage)", systemImage: "")
                        .foregroundColor(Color(.gray))
                        .accentColor(.gray)
                        .padding()
                        .frame(width: 250, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                }
                }
            }
            .padding(.bottom, 30)
            
            VStack(spacing: 1){ // 졸업유무 전공유형 스텍
                HStack(spacing: 30){
                    Text("졸업유무*")
                        .font(.system(size: 14))
                    HStack(spacing: 30){
                        Button(action: {
                            Graduate.toggle()
                        }) {
                            if Graduate {
                                ZStack{
                                    Circle()
                                        .fill(Color(red: 0.603, green: 0.756, blue: 0.819))
                                        .frame(width: 15, height: 15)
                                    Circle()
                                        .fill(Color.white)
                                        .frame(width: 6, height: 6)
                                }
                                Text("미졸업")
                            }
                            else {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 15, height: 15)
                                    .overlay(Circle().stroke(Color.gray, lineWidth: 1))
                                Text("미졸업")
                            }
                        }
                        .font(.system(size: 12))
                        .foregroundColor(.black)
                        
                        Button(action: {
                            Graduate.toggle()
                        }) {
                            if !Graduate {
                                ZStack{
                                    Circle()
                                        .fill(Color(red: 0.603, green: 0.756, blue: 0.819))
                                        .frame(width: 15, height: 15)
                                    Circle()
                                        .fill(Color.white)
                                        .frame(width: 6, height: 6)
                                }
                                Text("졸업")
                            }
                            else {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 15, height: 15)
                                    .overlay(Circle().stroke(Color.gray, lineWidth: 1))
                                Text("졸업")
                            }
                        }
                        .font(.system(size: 12))
                        .foregroundColor(.black)
                        Spacer()
                    }
                    .frame(width: 250, height: 50)
                }
                .padding(.bottom, 30)
                
                HStack(spacing: 30){
                    Text("전공유형*")
                        .font(.system(size: 14))
                    HStack(spacing: 20){
                        Button(action: {
                            Department = true
                            Major_minor = false
                            Double_major = false
                            Major1 = "IT융합자율학부"
                            Major2 = ""
                        }) {
                            if Department {
                                ZStack{
                                    Circle()
                                        .fill(Color(red: 0.603, green: 0.756, blue: 0.819))
                                        .frame(width: 15, height: 15)
                                    Circle()
                                        .fill(Color.white)
                                        .frame(width: 6, height: 6)
                                }
                                Text("전공 미선택")
                                    .foregroundColor(.black)
                                
                            }
                            else {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 15, height: 15)
                                    .overlay(Circle().stroke(Color.gray, lineWidth: 1))
                                Text("전공 미선택")
                                    .foregroundColor(.black)
                            }
                        }
                        .aspectRatio(contentMode: .fill)
                        Button(action: {
                            Department = false
                            Major_minor = true
                            Double_major = false
                            Major1 = "주전공"
                            Major2 = "부전공"
                        }) {
                            if Major_minor {
                                ZStack{
                                    Circle()
                                        .fill(Color(red: 0.603, green: 0.756, blue: 0.819))
                                        .frame(width: 15, height: 15)
                                    Circle()
                                        .fill(Color.white)
                                        .frame(width: 6, height: 6)
                                }
                                Text("주/부전공")
                                    .foregroundColor(.black)
                                
                            }
                            else {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 15, height: 15)
                                    .overlay(Circle().stroke(Color.gray, lineWidth: 1))
                                Text("주/부전공")
                                    .foregroundColor(.black)
                            }
                        }
                        .aspectRatio(contentMode: .fill)
                        Button(action: {
                            Department = false
                            Major_minor = false
                            Double_major = true
                            Major1 = "전공1"
                            Major2 = "전공2"
                        }) {
                            if Double_major {
                                ZStack{
                                    Circle()
                                        .fill(Color(red: 0.603, green: 0.756, blue: 0.819))
                                        .frame(width: 15, height: 15)
                                    Circle()
                                        .fill(Color.white)
                                        .frame(width: 6, height: 6)
                                }
                                Text("복수전공")
                                    .foregroundColor(.black)
                                
                            }
                            else {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 15, height: 15)
                                    .overlay(Circle().stroke(Color.gray, lineWidth: 1))
                                Text("복수전공")
                                    .foregroundColor(.black)
                            }
                        }
                        .aspectRatio(contentMode: .fill)
                    }
                    .font(.system(size: 12))
                    .foregroundColor(.black)
                    .frame(width: 250, height: 50)
                } // 졸업유무 전공유형 스택
                VStack(spacing: 1){
                    
                    HStack(spacing: 10){
                        if Department {
                            Text("\(Major1)")
                                .foregroundColor(.gray)
                                .padding()
                                .frame(width: 350, height: 50)
                                .background(Color(uiColor: .secondarySystemBackground))
                                .cornerRadius(10)
                        }
                        else if Major_minor {
                            HStack{
                                Menu("\(Major1)") {
                                    Button("소프트웨어공학",
                                           action: { Major1 = "소프트웨어공학"}
                                    )
                                    Button("정보통신공학",
                                           action: { Major1 = "정보통신공학"})
                                    Button("컴퓨터공학",
                                           action: { Major1 = "컴퓨터공학"})
                                    Button("인공지능",
                                           action: { Major1 = "인공지능"})
                                }
                                .foregroundColor(.gray)
                                .padding()
                                .frame(width: 170, height: 50)
                                .background(Color(uiColor: .secondarySystemBackground))
                                .cornerRadius(10)
                                
                                Menu("\(Major2)") {
                                    Button("소프트웨어공학",
                                           action: { Major2 = "소프트웨어공학"})
                                    Button("정보통신공학",
                                           action: { Major2 = "정보통신공학"})
                                    Button("컴퓨터공학",
                                           action: { Major2 = "컴퓨터공학"})
                                    Button("인공지능",
                                           action: { Major2 = "인공지능"})
                                }
                                .foregroundColor(.gray)
                                .padding()
                                .frame(width: 170, height: 50)
                                .background(Color(uiColor: .secondarySystemBackground))
                                .cornerRadius(10)
                            }
                        }
                        else if Double_major{
                            HStack{
                                Menu("\(Major1)") {
                                    Button("소프트웨어공학",
                                           action: { Major1 = "소프트웨어공학"})
                                    Button("정보통신공학",
                                           action: { Major1 = "정보통신공학"})
                                    Button("컴퓨터공학",
                                           action: { Major1 = "컴퓨터공학"})
                                    Button("인공지능",
                                           action: { Major1 = "인공지능"})
                                }
                                .foregroundColor(.gray)
                                .padding()
                                .frame(width: 170, height: 50)
                                .background(Color(uiColor: .secondarySystemBackground))
                                .cornerRadius(10)
                                
                                Menu("\(Major2)") {
                                    Button("소프트웨어공학",
                                           action: { Major2 = "소프트웨어공학"})
                                    Button("정보통신공학",
                                           action: { Major2 = "정보통신공학"})
                                    Button("컴퓨터공학",
                                           action: { Major2 = "컴퓨터공학"})
                                    Button("인공지능",
                                           action: { Major2 = "인공지능"})
                                }
                                .foregroundColor(.gray)
                                .padding()
                                .frame(width: 170, height: 50)
                                .background(Color(uiColor: .secondarySystemBackground))
                                .cornerRadius(10)
                            }
                        }
                    }
                    .frame(width: 350, height: 50)
                    HStack{ // 비밀번호 안내문구 출력
                        if Major1 == Major2{
                            Text(MarjorError)
                                .font(.system(size: 14))
                                .foregroundColor(.red)
                        }
                    }
                }
            }
            .padding(.bottom, 30)
            Spacer()
            Text("회원가입")
                .frame(width: 330, height: 10)
                .font(.headline)
                .foregroundColor(.white)
                .padding()
                .background(Color(red: 0.603, green: 0.756, blue: 0.819))
                .cornerRadius(10)
        }
    }
}
