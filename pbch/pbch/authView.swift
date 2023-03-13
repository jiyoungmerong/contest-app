//
//  authView.swift
//  pbch
//
//  Created by 문인호 on 2023/03/13.
//

import SwiftUI

struct authView: View {
    @State var student_id: String = ""
    @State var password: String = ""
    var body: some View {
            VStack{
                Image("skhuazbanner")
                    .ignoresSafeArea()
                HStack{
                    Text("본인인증")
                        .padding(.leading, 30)
                        .font(.system(size: 14))
                        .foregroundColor(.black)
                        .lineLimit(2)
                    Spacer()
                }
                TextField("아이디를 입력해주세요", text: $student_id)
                    .padding()
                    .frame(width: 350, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
                SecureField("비밀번호를 입력해주세요", text: $password)
                    .padding()
                    .frame(width: 350, height: 50)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .cornerRadius(10)
                NavigationLink(
                    destination: editProfileView(),
                    label: {
                        Text("로그인")
                            .frame(width: 330, height: 10)
                            .font(.headline)
                            .foregroundColor(.white)
                            .padding()
                            .background(Color(red: 0.603, green: 0.756, blue: 0.819))
                            .cornerRadius(10)
                        
                    })
                
                
                .padding(.top)
                HStack{
                    Spacer()
                    NavigationLink(
                        destination: FindView(),
                        label:{
                            Text("아이디/비밀번호 찾기")
                                .font(.system(size: 10))
                                .foregroundColor(Color.gray)
                        })
                    NavigationLink(
                        destination: SignUp(),
                        label:{
                            Text("회원가입")
                                .font(.system(size: 10))
                                .foregroundColor(Color.gray)
                                .padding(.trailing)
                        })
                    
                }
                Spacer()
            }
        }
    }

struct authView_Previews: PreviewProvider {
    static var previews: some View {
        authView()
    }
}
