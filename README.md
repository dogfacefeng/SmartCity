# SmartCity
原生Android实现移动应用开发竞赛中的照片上传和相册选择功能，无任何权限和File Provider内容。适合竞赛代码使用，各位参加竞赛学生的福音。更多要求欢迎在ISSUE中提😀；讨论技术欢迎加微信：caleb199701；
## 使用注意点
1.该部分为省事情，token直接使用明文存储在MyApplication中，需要更换
2.不要在manifest中申明相机权限，否则直接使用Intent过去会报错
