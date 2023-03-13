//
//  MainView.swift
//  pbch
//
//  Created by 문인호 on 2023/03/13.
//

import SwiftUI

struct MainView: View {
    @Binding var index: Int
    var body: some View {
        NavigationView{
            VStack{
                Spacer()
                HStack{
                    Image(systemName: "apple.logo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 150, height: 150)
                        .padding(.trailing)
                    VStack(alignment: .leading, spacing: 5){
                        HStack{
                            Text("201814077")
                            Text("천상구")
                        }
                        Text("소프트웨어공학전공")
                        Text("정보통신공학전공")
                        Text("3학년 1학기")
                    }
                }
                .padding(.bottom)
                HStack{
                    Text("내가 최근 쓴 강의평")
                        .padding(.leading, 50)
                    Spacer()
                    //                    NavigationLink(destination: EvaluationView()) {
                        //                        Text("더보기")
                    //                    }
                    Button{
                        self.index = 1
                    } label:{
                        Text("더보기")
                    }
                    .padding(.trailing, 50)
                }
                .font(.system(size: 15))
                .foregroundColor(Color.gray)
                .padding(.top, 30)
                Divider()
                    .background(Color.gray)
                    .frame(width: 310)
                    .padding(.top, 0)
                HStack{
                    Text("강의명 | 교수님명")
                        .padding(.leading, 5)
                        .foregroundColor(Color.white)
                    Spacer()
                    Text("점수 표기")
                        .padding(.trailing, 5)
                        .foregroundColor(Color.white)
                    
                }
                .frame(width: 310, height: 40)
                .background(Color.pink)
                HStack{
                    Text("강의명 | 교수님명")
                        .padding(.leading, 5)
                        .foregroundColor(Color.white)
                    Spacer()
                    Text("점수 표기")
                        .padding(.trailing, 5)
                        .foregroundColor(Color.white)
                    
                }
                .frame(width: 310, height: 40)
                .background(Color.pink)
                HStack{
                    Text("강의명 | 교수님명")
                        .padding(.leading, 5)
                        .foregroundColor(Color.white)
                    Spacer()
                    Text("점수 표기")
                        .padding(.trailing, 5)
                        .foregroundColor(Color.white)
                    
                }
                .frame(width: 310, height: 40)
                .background(Color.pink)
                Spacer()
            }
        }
    }
}
//
//    struct MainView_Previews: PreviewProvider {
//        @Binding var index: Int
//        static var previews: some View {
//            MainView()
//        }
//    }
