////
////  make2.swift
////  SKHUAZ
////
////  Created by 천성우 on 2023/03/13.
////
//
//import SwiftUI
//
//struct make2:View {
//    @State var point: Int = 4
//    var body: some View{
//        VStack(alignment:.leading){
//            Text("5점 기준 그래프")
//                .frame(width: 260, height: 15)
//                .background(Color.blue)
//            Text("3점 기준 그래프")
//                .frame(width: 156, height: 15)
//                .background(Color.blue)
//            Text("1점")
//                .frame(width: 52, height: 15)
//                .background(Color.blue)
//            chartView(point: $point)
//        }
//        .frame(width: 260, height: 90)
//        .background(Color(uiColor: .secondarySystemBackground))
//        
//    }
//}
//
//struct make2_Previews: PreviewProvider {
//    static var previews: some View {
//        make2()
//    }
//}
////}
////struct chartView: View{
////    @Binding var point: Int
////    var body: some View{
////        if point == 1 {
////            Text("1")
////                .frame(width: 52, height: 15)
////                .background(Color.blue)
////        }
////        else if point == 2 {
////            Text("2")
////                .frame(width: 104, height: 15)
////                .background(Color.blue)
////        }
////        else if point == 3 {
////            Text("3")
////                .frame(width: 156, height: 15)
////                .background(Color.blue)
////        }
////        else if point == 4 {
////            Text("4")
////                .frame(width: 208, height: 15)
////                .background(Color.blue)
////        }
////        else if point == 5 {
////            Text("5점테스트")
////            .frame(width: 260, height: 15)
////            .background(Color.blue)
////        }
////    }
////}
////
////
