import SwiftUI

struct SignUpView: View {
    @State private var Name: String = "" // 이름
    @State private var Nickname: String = "" // 닉네임
    @State private var Student_id: String = "" // 학번
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
                    Student_id: self.$Student_id,
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
    @Binding var Nickname: String
    @Binding var Student_id: String
    @Binding var Password: String
    @Binding var RepeatedPassword: String
    @Binding var passwordError: String
    @Binding var Semester: Int
    
    @State var SemesterMessage: String = "재학중인 학기를 선택하시오"
    @State var CheckMessage: String = "ex) abc123@office.skhu.ac.kr"
    @State var ShowModel: Bool = false
    @State var certification: Bool = false
    
    
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
    
    @Binding var Graduate: Bool
    @Binding var Department: Bool
    @Binding var Major_minor: Bool
    @Binding var Double_major: Bool
    @Binding var Major1: String
    @Binding var Major2: String
    @State var MarjorError: String = "선택한 두 전공이 같습니다"
    
    struct ModalView: View {
        @State private var test: String = ""
        @Environment(\.presentationMode) var presentatio
        
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
                        TextField("인증번호 입력", text: $test)
                            .frame(width: 350, height: 50)
                            .textFieldStyle(.roundedBorder)
                        HStack{
                            Button(action: {
                                // 인증번호 재전송하는 기능
                            }, label: {
                                Image(systemName: "arrow.clockwise")
                                Text("인증코드 재전송")
                            })
                            .foregroundColor(.gray)
                            Spacer()
                        }
                        .padding(.leading, 30)
                        
                    }
                    HStack{
                        Button(action: {
                            presentatio.wrappedValue.dismiss()
                        }) {
                            Text("취소").bold()
                        }
                        .foregroundColor(.gray)
                        .frame(width: 170, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                        Button(action: {
                            // 인증번호 확인
                        }) {
                            Text("확인").bold()
                        }
                        .foregroundColor(.white)
                        .frame(width: 170, height: 50)
                        .background(Color(red: 0.603, green: 0.756, blue: 0.819))
                        .cornerRadius(10)
                    }
                }
                
            }
        }
    }
    
    var body: some View {
        ScrollView{
            Spacer()
                .padding(30)
            TextField("이름을 입력해주세요* ", text: $Name)
                .padding()
                .frame(width: 350, height: 50)
                .background(Color(uiColor: .secondarySystemBackground))
                .cornerRadius(10)
            HStack{
                TextField("닉네임을 입력해주세요* ", text: $Nickname)
                    .padding()
                    .frame(width: 250, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
                Button{} label: {
                    Text("중복확인")
                        .foregroundColor(Color(red: 0.76, green: 0.552, blue: 0.552))
                        .frame(width: 90, height:50)
                        .background(RoundedRectangle(cornerRadius: 10).strokeBorder(Color(red: 0.76, green: 0.552, blue: 0.552)))
                }
            } // 닉네임 입력 HStack
            .padding(30)
            VStack{
                HStack{
                    TextField("학교이메일을 입력해주세요* ", text: $Student_id)
                        .padding()
                        .frame(width: 250, height: 50)
                        .background(Color(uiColor: .secondarySystemBackground))
                        .cornerRadius(10)
                        .keyboardType(.decimalPad)
                    Button{
                        if checkEmail(str: Student_id){
                            CheckMessage = ""
                            self.ShowModel = true
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
                                ModalView()
                            }
                        
                    }
                }
                HStack{
                    Text("\(CheckMessage)")
                        .padding(.leading, 30)
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
        }//VStack 종료 부분
        .padding(.trailing, 30)
        .padding(.leading, 30)
    }
}

