//
//  EvaluationView.swift
//  pbch
//
//  Created by 문인호 on 2023/03/13.
//

import SwiftUI

struct EvaluationView: View {
    
    @State var click1: Bool = false
    @State var click2: Bool = false
    @State var click3: Bool = false
    @State var text : String = ""
    
    var body: some View {
        VStack{
            HStack{
                Image(systemName: "apple.logo")
                    .padding(.horizontal, 20)
                VStack{
                    searchBar(text: self.$text)
                }
            }
            .padding(.bottom, 8)
            HStack{
                Spacer()
            Button(action: {
                self.click1.toggle()
            }) {
                if self.click1{
                    Text("전공필수")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(5)
                        .background(.black)
                        .cornerRadius(10)
                }
                else{
                    Text("전공필수")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(5)
                        .background(.gray)
                        .cornerRadius(10)
                }
            }
            Spacer()
            Button(action: {
                self.click2.toggle()
            }) {
                if self.click2{
                    Text("전공선택")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(5)
                        .background(.black)
                        .cornerRadius(10)
                }
                else{
                    Text("전공선택")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(5)
                        .background(.gray)
                        .cornerRadius(10)
                }
            }
            Spacer()

            Button(action: {
                self.click3.toggle()
            }) {
                if self.click3{
                    Text("전공탐색")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(5)
                        .background(.black)
                        .cornerRadius(10)
                }
                else{
                    Text("전공필수")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(5)
                        .background(.gray)
                        .cornerRadius(10)
                }
            }
                Spacer()

            
        }
            List(0..<20){ i in
                Text("\(i)")
            }
        }
    }
    
}

struct EvaluationView_Previews: PreviewProvider {
    static var previews: some View {
        EvaluationView()
    }
}

struct searchBar: View {
    //Binding은 외부에서 값을 바인딩시킬수있다.
    //택스트필드에 들어갈 값을 저장하기위한 변수
    @Binding var text : String
    @State var editText : Bool = false
    
    var body: some View {
        HStack{
            //검색창을 받을수있는 택스트필드
            TextField("검색어를 넣어주세요" , text : self.$text)
                .padding(15)
                .padding(.horizontal,15)
                .background(Color(.systemGray6))
                .cornerRadius(15)
                .overlay(
                    HStack{
                        Spacer()
                        if self.editText{
                            Button(action : {
                                self.editText = false
                                self.text = ""
                                UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                            }){
                                Image(systemName: "multiply.circle.fill")
                                    .foregroundColor(Color(.black))
                                    .padding()
                            }
                        }else{
                            Image(systemName: "magnifyingglass")
                                .foregroundColor(Color(.black))
                                .padding()
                        }
                       
                    }
                ).onTapGesture {
                    self.editText = true
                }
        }
        
        
    }
}

