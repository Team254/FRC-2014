require "rubygems"
require 'net/ftp'
require "stringio"

ftp = Net::FTP.new
ftp.open_timeout = 5

begin
  ftp.connect "10.2.54.2"
rescue
  puts "FTP not working. Check your wifi settings.";
  exit
end

status = StringIO.new
ftp.chdir('www')
ftp.gettextfile('git-hash', status)
commit_hash = status.string


commit_hash = `git log --reverse --format=%H | head -n 1`.strip if commit_hash.nil? || commit_hash == ""

changed_files = `git diff --name-only #{commit_hash} app/`.split("\n")
modified_files = `git ls-files -m src/`.split("\n")
new_files = `git ls-files --others --exclude-standard app/`.split("\n")
all_files = (changed_files + modified_files + new_files).uniq

all_files.each do |f|
  puts "Sending #{f}"
  ftp.puttextfile(f,f)
end

# figure out how to put the commit hash later

