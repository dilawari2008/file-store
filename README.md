# Large File Uploader
### Can upload files upto 1GB

TODOs

paginate get api
search api4
authentication

write a v2 for upload
file size limit
take environment variables out
proper error handling
correct modelling
correct structure of AWS repositories
set log levels

error msgs as file already exists

index for search
util to calculate file type, or see if can be done using S3Object class
refer example for better structuring using interfaces
upsert based on filename
linting
make upload and download asynchronous in Completeable future
switch app.props to app.yml
upload status in db, and accordingly modify the get api
split upload mechanisms based on file size
progress of file upload
add websockets/webhooks for file upload progress bar on frontend
request timeout for http request
file size in model
Add logs
Trap cURLS
see if any changes can be done on download part
use ProgressListener instead of while loop
code to know where the multipart arrived
observe cpu/ memory usage when using TransferManager class instead PutObject

post authentication - allow only 1 file name per user

look into the location of heap and how much memory is being consumed
