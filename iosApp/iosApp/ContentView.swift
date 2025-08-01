//
//  ContentView.swift
//  iosApp
//
//  Created by Luca Marchi on 01/08/25.
//

import SwiftUI
import sharedKit

struct ContentView: View {
    var body: some View {
        Text(Greeting().greet())
        .padding()
    }
}

#Preview {
    ContentView()
}
