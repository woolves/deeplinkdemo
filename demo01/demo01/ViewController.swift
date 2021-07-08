//
//  ViewController.swift
//  demo01
//
//  Created by 石康暘 on 2021/7/8.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        if let apiKey = Bundle.main.object(forInfoDictionaryKey: "demo") as? String {
            print(apiKey)
            let label = UILabel()
            label.frame = CGRect(x: 150, y: 150, width: 100, height: 20)
            view.addSubview(label)
            label.text = apiKey
        }
        print ("ok")
    }
}

