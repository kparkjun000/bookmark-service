# Heroku Deployment Guide

This guide will walk you through deploying the Shopping Mall API to Heroku.

## Prerequisites

1. [Heroku Account](https://signup.heroku.com/)
2. [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) installed
3. Git installed

## Step-by-Step Deployment

### 1. Initialize Git Repository (if not already done)

```bash
git init
git add .
git commit -m "Initial commit"
```

### 2. Login to Heroku

```bash
heroku login
```

### 3. Create a New Heroku App

```bash
heroku create your-shopping-api
```

Replace `your-shopping-api` with your desired app name. If you don't specify a name, Heroku will generate one for you.

### 4. Add PostgreSQL Database

```bash
heroku addons:create heroku-postgresql:essential-0
```

This creates a PostgreSQL database and automatically sets the `DATABASE_URL` environment variable.

### 5. Verify Database Configuration

```bash
heroku config
```

You should see the `DATABASE_URL` in the output.

### 6. Configure Application for Heroku

The application is already configured to use Heroku's `DATABASE_URL` environment variable. The `application.yml` file uses `${DATABASE_URL}` with a fallback to localhost for local development.

### 7. Deploy to Heroku

```bash
git push heroku main
```

If your default branch is named `master`, use:

```bash
git push heroku master
```

### 8. Scale Your Dynos

```bash
heroku ps:scale web=1
```

### 9. Open Your Application

```bash
heroku open
```

Add `/swagger-ui.html` to the URL to access the API documentation:

```
https://your-shopping-api.herokuapp.com/swagger-ui.html
```

## Environment Variables

The application automatically reads the following environment variables from Heroku:

- `DATABASE_URL` - PostgreSQL connection string (automatically set by Heroku)
- `PORT` - Application port (automatically set by Heroku)

To set custom environment variables:

```bash
heroku config:set VARIABLE_NAME=value
```

## Database Migrations

The application is configured with `ddl-auto: update` which will automatically update the database schema. For production, you might want to use `validate` and manage migrations separately.

## View Logs

To view application logs:

```bash
heroku logs --tail
```

## Restart Application

If you need to restart the application:

```bash
heroku restart
```

## Common Issues and Solutions

### Issue: Application crashes on startup

**Solution**: Check the logs for errors

```bash
heroku logs --tail
```

### Issue: Database connection fails

**Solution**: Verify DATABASE_URL is set

```bash
heroku config:get DATABASE_URL
```

### Issue: Out of memory errors

**Solution**: Upgrade to a larger dyno

```bash
heroku ps:resize web=standard-1x
```

## Monitoring

### Check Application Status

```bash
heroku ps
```

### View Resource Usage

```bash
heroku addons
```

### Access PostgreSQL

```bash
heroku pg:psql
```

## Continuous Deployment

### Option 1: GitHub Integration

1. Go to your Heroku Dashboard
2. Select your app
3. Click on the "Deploy" tab
4. Connect to GitHub
5. Enable automatic deploys from your main branch

### Option 2: Manual Deployment

```bash
git add .
git commit -m "Your commit message"
git push heroku main
```

## Production Recommendations

1. **Enable Production Profile**

```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

2. **Scale Your Application**

```bash
heroku ps:scale web=2
```

3. **Add Monitoring** (optional)

```bash
heroku addons:create newrelic:wayne
```

4. **Configure Automated Backups**

```bash
heroku pg:backups:schedule DATABASE_URL --at '02:00 America/Los_Angeles'
```

5. **Set Up SSL** (Automatic with Heroku)

All Heroku apps automatically get SSL certificates. Access your app via `https://`.

## Cost Estimation

- **Eco Dyno**: $5/month (sleeps after 30 minutes of inactivity)
- **Basic Dyno**: $7/month (never sleeps)
- **Essential PostgreSQL**: $5/month
- **Standard Dyno**: Starting at $25/month (for production)

## Useful Commands

```bash
# View app info
heroku info

# Run commands on Heroku
heroku run bash

# Reset database (WARNING: Deletes all data!)
heroku pg:reset DATABASE_URL --confirm your-app-name

# View database info
heroku pg:info

# Create database backup
heroku pg:backups:capture

# Download latest backup
heroku pg:backups:download

# Connect to PostgreSQL
heroku pg:psql

# View application metrics
heroku metrics
```

## Support

For more information, visit the [Heroku Dev Center](https://devcenter.heroku.com/).
