:: 用于提交当前变更(windows)
:: author: Brath
:: LastUpdateTime:  2023-12-27 20:15:03
:: 用法：双击运行，或者当前路径 cmd 直接输入 .\cgit.bat

git pull
git add .
git commit -m "[Feature] add for new"
git push
git status

