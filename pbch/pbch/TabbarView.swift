//
//  TabbarView.swift
//  pbch
//
//  Created by 문인호 on 2023/03/13.
//

import SwiftUI

struct TabbarView: View {
    @State private var index = 3
    
    var body: some View {
        TabView(selection: $index) {
            EvaluationView()
                .tabItem {
                  Image(systemName: "1.square.fill")
                  Text("강의평")
                }
                .background()
                .tag(1)
              CurriculumView()
                .tabItem {
                  Image(systemName: "2.square.fill")
                  Text("커리큘럼")
                }
                .tag(2)
            MainView(index: $index)
                .tabItem {
                  Image(systemName: "apple.logo")
                  Text("LOGO")
                }
                .tag(3)
            RootView()
              .tabItem {
                Image(systemName: "2.square.fill")
                Text("루트추천")
              }
              .tag(4)
            SettingView()
              .tabItem {
                Image(systemName: "gearshape")
                Text("설정")
              }
              .tag(5)
            
            }
            .font(.headline)
            .navigationBarHidden(true)
    }
}

struct TabbarView_Previews: PreviewProvider {
    static var previews: some View {
        TabbarView()
    }
}
